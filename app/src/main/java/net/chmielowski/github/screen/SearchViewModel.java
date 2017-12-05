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
    private String lastQuery;

    @Inject
    SearchViewModel(final ReposRepository repository) {
        this.repository = repository;
    }

    public Observer<String> search() {
        return justSearchSubject;
    }

    public static class Query {
        public String text;
        public int page;

        Query(final Integer page, final String text) {
            this.page = page;
            this.text = text;
        }

        @NonNull
        private static Query firstPage(final String query) {
            return new Query(0, query);
        }
    }


    public Observable<ListState> searchResults(final Observable<?> searchBtnClicked,
                                               final Observable<String> searchQuery,
                                               final Observable<?> scrolledToEnd,
                                               final Observable<String> observeQuery) {
        return searchResults(
                Observable.merge(
                        searchQuery,
                        searchBtnClicked.withLatestFrom(observeQuery, (__, query) -> query)),
                scrolledToEnd);
    }

    // TODO: eliminate loading field
    private Observable<ListState> searchResults(final Observable<String> searchQuery,
                                                final Observable<?> scrolledToEnd) {
        return Observable.merge(
                searchQuery
                        .doOnNext(query -> lastQuery = query)
                        .map(Query::firstPage),
                scrolledToEnd
                        .map(__ -> new Query(page++, lastQuery)) // TODO: mutable

        )

                .filter(__ -> notLoadingCurrently())
                .doOnNext(__ -> searchHistoryVisible.set(false))
                .doOnNext(this::addToHistory)
                .flatMap(q ->
                        repository.items(q)
                                .map(repositories -> repositories.stream()
                                        .map(repo -> new RepositoryViewModel(repo, q.text))
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
            queryHistory.searched(query.text);
        }
    }

    public Observable<Collection<String>> searches() {
        return queryHistory.observe();
    }
}
