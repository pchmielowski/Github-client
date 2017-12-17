package net.chmielowski.github.screen;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import net.chmielowski.github.data.RepoService;
import net.chmielowski.github.network.NetworkState;
import net.chmielowski.github.utils.Assertions;
import net.chmielowski.github.utils.ValueIgnored;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Singleton
public final class SearchViewModel {
    private final RepoService repository;

    private final QueryHistory queryHistory;

    public final ObservableBoolean searchMode = new ObservableBoolean(true);

    private int page = 0;
    private String lastQuery;
    private boolean isLoading;

    @Inject
    SearchViewModel(@RepoService.OnMainThread final RepoService repository,
                    final QueryHistory queryHistory,
                    final NetworkState networkState) {
        this.repository = repository;
        this.queryHistory = queryHistory;
        this.networkState = networkState;
    }

    public Observable<ListState> replaceResults(final Observable<?> searchBtnClicked,
                                                final Observable<String> searchQuery,
                                                final Observable<CharSequence> observeQuery) {
        return replaceResults(
                Observable.merge(
                        searchQuery,
                        searchBtnClicked.withLatestFrom(observeQuery, (__, query) -> query)));
    }

    // TODO: eliminate loading field
    public Observable<ListState> appendResults(final Observable<ValueIgnored> scrolledToEnd) {
        return scrolledToEnd
                .doOnNext(__ -> requireNonNull(lastQuery))
                .compose(Assertions::neverCompletes)
                .filter(__ -> canLoad())
                .doOnNext(__ -> page++)
                .map(__ -> new Query(page, lastQuery))
                .flatMap(this::fetchResults);
    }

    Observable<ListState> replaceResults(final Observable<CharSequence> searchQuery) {
        // TODO: handle null/empty query
        return searchQuery
                .compose(Assertions::neverCompletes)
                .map(CharSequence::toString)
                .filter(__ -> networkState.isOnline())
                .doOnNext(this::updateView)
                .map(Query::firstPage)
                .flatMap(this::fetchResults);
    }

    private void updateView(final String query) {
        lastQuery = query;
        page = 0;
        searchMode.set(false);
        queryHistory.searched(query);
    }

    private final NetworkState networkState;

    @SuppressWarnings("Convert2MethodRef")
    private Observable<ListState> fetchResults(final Query query) {
        return repository.items(query)
                .map(repositories -> repositories.stream()
                        .map(repo -> new RepositoryViewModel(repo, query.text))
                        .collect(toList()))
                .map(ListState::loaded)
                .toObservable()
                .startWith(ListState.loading())
                .doOnSubscribe(__ -> lock())
                .doOnComplete(() -> unlock());
    }

    private void unlock() {
        isLoading = false;
    }

    private void lock() {
        isLoading = true;
    }

    private boolean canLoad() {
        return !isLoading;
    }

    public void enterSearchMode() {
        searchMode.set(true);
    }

    public void exitSearchMode() {
        searchMode.set(false);
    }

    @EqualsAndHashCode
    @ToString
    public static class Query {
        public String text;

        public int page;

        Query(final int page, @NonNull final String text) {
            requireNonNull(text);
            this.page = page;
            this.text = text;
        }

        @NonNull
        public static Query firstPage(final String query) {
            return new Query(0, query);
        }


    }

}
