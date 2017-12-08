package net.chmielowski.github.screen;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.jakewharton.rxbinding2.InitialValueObservable;

import net.chmielowski.github.data.ReposRepository;
import net.chmielowski.github.pagination.ValueIgnored;
import net.chmielowski.github.utils.Assertions;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static java.util.Objects.requireNonNull;

public final class SearchViewModel {
    private final ReposRepository repository;

    private final Subject<String> justSearchSubject = PublishSubject.create(); // TODO: rename

    public final ObservableBoolean searchVisible = new ObservableBoolean(false);
    public final ObservableBoolean searchHistoryVisible = new ObservableBoolean(true);

    private int page = 0; // TODO: can we avoid this mutable variable?
    private String lastQuery;
    private boolean locked;

    @Inject
    SearchViewModel(final ReposRepository repository, final QueryHistory queryHistory) {
        this.repository = repository;
        this.queryHistory = queryHistory;
    }

    public Observer<String> search() {
        return justSearchSubject;
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
        static Query firstPage(final String query) {
            return new Query(0, query);
        }
    }


    public Observable<ListState> replaceResults(final Observable<?> searchBtnClicked,
                                                final Observable<String> searchQuery,
                                                final Observable<String> observeQuery) {
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

    // TODO: remove suppression
    @SuppressWarnings("WeakerAccess")
    Observable<ListState> replaceResults(final Observable<String> searchQuery) {
        return searchQuery
                .compose(Assertions::neverCompletes)
                .doOnNext(query -> {
                    lastQuery = query;
                    page = 0;
                    searchHistoryVisible.set(false);
                    queryHistory.searched(query);
                })
                .map(Query::firstPage)
                .flatMap(this::fetchResults)
                .startWith(ListState.initial());
    }

    @SuppressWarnings("Convert2MethodRef")
    private Observable<ListState> fetchResults(final Query query) {
        return repository.items(query)
                .map(repositories -> repositories.stream()
                        .map(repo -> new RepositoryViewModel(repo, query.text))
                        .collect(Collectors.toList()))
                .map(ListState::loaded)
                .toObservable()
                .startWith(ListState.loading())
                .doOnSubscribe(__ -> lock())
                .doOnComplete(() -> unlock());
    }

    private void unlock() {
        locked = false;
    }

    private void lock() {
        locked = true;
    }

    private boolean canLoad() {
        return !locked;
    }

    public Disposable searchVisibleDisposable(final InitialValueObservable<CharSequence> observable) {
        return observable.subscribe(query -> {
            searchHistoryVisible.set(true);
            searchVisible.set(query.length() > 0);
        });
    }

    public void clear() {
        searchHistoryVisible.set(false);
    }

    private final QueryHistory queryHistory;

    public Observable<Collection<String>> searches() {
        return queryHistory.observe();
    }
}
