package net.chmielowski.github.screen;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import net.chmielowski.github.data.ReposRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public final class SearchViewModel {
    private final ReposRepository repository;

    private final Subject<CharSequence> querySubject = PublishSubject.create();
    private final Subject<String> justSearchSubject = PublishSubject.create(); // TODO: rename

    public final ObservableBoolean inputVisible = new ObservableBoolean(true);
    public final ObservableBoolean searchVisible = new ObservableBoolean(false);
    public final ObservableBoolean searchHistoryVisible = new ObservableBoolean(true);
    public final ObservableBoolean loading = new ObservableBoolean(false);

    private int page = 0; // TODO: can we avoid this mutable variable?

    @Inject
    SearchViewModel(final ReposRepository repository) {
        this.repository = repository;
    }

    public Observer<CharSequence> queryChanged() {
        return querySubject;
    }

    public Observer<String> search() {
        return justSearchSubject;
    }

    static class Query {
        String query;
        int page;

        Query(final Integer page, final String query) {
            this.page = page;
            this.query = query;
        }

        @NonNull
        private static Query firstPage(final String query) {
            return new Query(0, query);
        }
    }

    // TODO: eliminate loading field
    // TODO: try to eliminate subjects by just passing observables as this method's parameter
    public Observable<ListState> searchResults(final Observable<?> searchBtnClicked,
                                               final Observable<String> searchQuery,
                                               final Observable<?> scrolledToEnd) {
        return Observable.merge(
                searchBtnClicked
                        .map(__ -> 0)
                        .withLatestFrom(observeQuery(), Query::new)
                ,
                searchQuery
                        .map(Query::firstPage),
                scrolledToEnd
                        .map(__ -> page++)
                        .withLatestFrom(observeQuery(), Query::new)

        )

                .filter(__ -> notLoadingCurrently())
                .doOnNext(__ -> searchHistoryVisible.set(false))
                .doOnNext(this::addToHistory)
                .flatMap(q ->
                        repository.items(q.query, q.page)
                                .map(repositories -> repositories.stream()
                                        .map(repo -> new RepositoryViewModel(repo, q.query))
                                        .collect(Collectors.toList()))
                                .map(results -> new ListState(results, false)) // TODO: factory method
                                .toObservable()
                                .startWith(new ListState(Collections.emptyList(), true))
                                .doOnSubscribe(__ -> loading.set(true))
                                .doOnComplete(() -> loading.set(false))
                )
                .startWith(new ListState(Collections.emptyList(), false)); // TODO: factory method
    }

    private boolean notLoadingCurrently() {
        return !loading.get();
    }

    public Disposable searchVisibleDisposable() {
        return observeQuery().subscribe(query -> {
            searchHistoryVisible.set(true);
            searchVisible.set(!query.isEmpty());
        });
    }

    private Observable<String> observeQuery() {
        return querySubject
                .doOnComplete(this::throwBadState)
                .map(String::valueOf);
    }

    private void throwBadState() {
        throw new IllegalStateException("subject completed");
    }

    class QueryHistory {
        // TODO: store subject Realm
        Collection<String> history = new LinkedList<>();
        Subject<String> subject = PublishSubject.create();

        void searched(String query) {
            history.add(query);
            subject.onNext("");
        }

        Observable<Collection<String>> observe() {
            return subject.map(__ -> Collections.unmodifiableCollection(history));
        }
    }

    // TODO: inject
    private final QueryHistory queryHistory = new QueryHistory();

    private void addToHistory(final Query query) {
        if (query.page == 0) {
            queryHistory.searched(query.query);
        }
    }

    public Observable<Collection<String>> searches() {
        return queryHistory.observe();
    }
}
