package net.chmielowski.github.screen;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Pair;

import net.chmielowski.github.data.ReposRepository;
import net.chmielowski.github.pagination.ValueIgnored;

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
    private final Subject<Object> searchSubject = PublishSubject.create();
    private final Subject<Object> justSearchSubject = PublishSubject.create(); // TODO: rename
    private final Subject<ValueIgnored> scrolledToEndSubject = PublishSubject.create();

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

    public Observer<Object> searchClicked() {
        return searchSubject;
    }

    public Observer<Object> search() {
        return justSearchSubject;
    }

    // TODO: eliminate loading field
    // TODO: try to eliminate subjects by just passing observables as this method's parameter
    public Observable<ListState> searchResults() {
        return observeSearchClicked()
                .mergeWith(justSearchSubject)
                .mergeWith(observeScrolledToEnd())
                .map(__ -> page)
                .doOnNext(__ -> searchHistoryVisible.set(false))
                .withLatestFrom(observeQuery(), Pair::create)
                .doOnNext(this::addToHistory)
                .flatMap(q ->
                        repository.items(q.second, page)
                                .map(repositories -> repositories.stream()
                                        .map(repo -> new RepositoryViewModel(repo, q.second))
                                        .collect(Collectors.toList()))
                                .map(results -> new ListState(results, false)) // TODO: factory method
                                .toObservable()
                                .startWith(new ListState(Collections.emptyList(), true))
                                .doOnSubscribe(__ -> loading.set(true))
                                .doOnComplete(() -> loading.set(false))
                )
                .startWith(new ListState(Collections.emptyList(), false)); // TODO: factory method
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

    private Observable<Object> observeSearchClicked() {
        return searchSubject
                .doOnComplete(this::throwBadState);
    }

    @NonNull
    private Observable<Integer> observeScrolledToEnd() {
        return scrolledToEndSubject
                .filter(__ -> !loading.get())
                .doOnNext(__ -> page++)
                .doOnComplete(this::throwBadState)
                .map(__ -> page);
    }

    public Observer<ValueIgnored> scrolledCloseToEnd() {
        return scrolledToEndSubject;
    }

    public Observable<Boolean> observeLoading() {
        return null;
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

    private void addToHistory(final Pair<Integer, String> query) {
        queryHistory.searched(query.second);
    }

    public Observable<Collection<String>> searches() {
        return queryHistory.observe();
    }
}
