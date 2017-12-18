package net.chmielowski.github.screen;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import net.chmielowski.github.data.RepositoryDataSource;
import net.chmielowski.github.network.NetworkState;
import net.chmielowski.github.utils.Assertions;
import net.chmielowski.github.utils.ValueIgnored;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Singleton
public final class SearchViewModel {
    private final RepositoryDataSource repository;

    private final QueryHistory queryHistory;

    public final ObservableField<String> query = new ObservableField<>();
    public final ObservableBoolean searchMode = new ObservableBoolean(true);

    private int page = 0;
    private String lastQuery;
    private boolean isLoading;

    @Inject
    SearchViewModel(@RepositoryDataSource.WorkOnBackground final RepositoryDataSource repository,
                    final QueryHistory queryHistory,
                    final NetworkState networkState) {
        this.repository = repository;
        this.queryHistory = queryHistory;
        this.networkState = networkState;
    }

    public Observable<ListState> replaceResults(final Observable<?> searchBtnClicked,
                                                final Observable<String> searchQuery) {
        return replaceResults(
                Observable.merge(
                        searchQuery,
                        searchBtnClicked.map(__ ->
                                requireNonNull(query.get(), "Query is null"))));
    }

    Observable<ListState> replaceResults(final Observable<CharSequence> searchQuery) {
        // TODO: handle null/empty query
        return searchQuery
                .compose(Assertions::neverCompletes)
                .map(CharSequence::toString)
                .filter(__ -> networkState.isOnline())
                .doOnNext(this::updateState)
                .map(Query::firstPage)
                .flatMap(this::fetchResults);
    }

    public Observable<ListState> appendResults(final Observable<ValueIgnored> scrolledToEnd) {
        return scrolledToEnd
                .doOnNext(__ -> requireNonNull(lastQuery))
                .compose(Assertions::neverCompletes)
                .filter(__ -> isNotLoading())
                .doOnNext(__ -> page++)
                .map(__ -> new Query(page, lastQuery))
                .flatMap(this::fetchResults);
    }

    private void updateState(final String text) {
        query.set("");
        lastQuery = text;
        page = 0;
        if (!searchMode.get()) {
            throw new IllegalStateException("Not in search mode");
        }
        searchMode.set(false);
        queryHistory.searched(text);
    }

    private final NetworkState networkState;

    @SuppressWarnings("Convert2MethodRef")
    private Observable<ListState> fetchResults(final Query query) {
        return repository.repositories(query)
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

    private boolean isNotLoading() {
        return !isLoading;
    }

    public void enterSearchMode() {
        if (searchMode.get()) {
            throw new IllegalStateException("Already in search mode");
        }
        searchMode.set(true);
    }

    public void exitSearchMode() {
        if (!searchMode.get()) {
            throw new IllegalStateException("Not in search mode");
        }
        searchMode.set(false);
    }

    public Single<Boolean> onBackPressed() {
        final boolean changeMode = searchMode.get() && lastQuery != null;
        if (changeMode) {
            searchMode.set(false);
        }
        return Single.just(changeMode);
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
