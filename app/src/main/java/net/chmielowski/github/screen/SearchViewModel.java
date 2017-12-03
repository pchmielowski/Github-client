package net.chmielowski.github.screen;

import android.databinding.ObservableBoolean;

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
    private final Subject<Object> searchSubject = PublishSubject.create();

    public final ObservableBoolean inputVisible = new ObservableBoolean(true);
    public final ObservableBoolean searchVisible = new ObservableBoolean(false);
    public final ObservableBoolean searchHistoryVisible = new ObservableBoolean(true);
    public final ObservableBoolean loading = new ObservableBoolean(false);

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

    public Observable<Collection<RepositoryViewModel>> searchResults() {
        return observeSearchClicked()
                .doOnNext(__ -> searchHistoryVisible.set(false))
                .withLatestFrom(observeQuery(), (__, s) -> s)
                .doOnNext(this::addToHistory)
                .flatMapSingle(query ->
                        repository.items(query)
                                .map(repositories -> repositories.stream()
                                        .map(repo -> new RepositoryViewModel(repo, query))
                                        .collect(Collectors.toList()))
                                .doOnSubscribe(__ -> loading.set(true))
                                .doOnSuccess(__ -> loading.set(false))
                                .doOnSuccess(__ -> searchVisible.set(false)));
    }

    public Disposable searchVisibleDisposable() {
        return observeQuery().subscribe(query -> {
            searchHistoryVisible.set(true);
            searchVisible.set(!query.isEmpty());
        });
    }

    private Observable<String> observeQuery() {
        return querySubject
                .doOnComplete(() -> {
                    throw new IllegalStateException("querySubject completed");
                })
                .map(String::valueOf);
    }

    private Observable<Object> observeSearchClicked() {
        return searchSubject
                .doOnComplete(() -> {
                    throw new IllegalStateException("searchSubject completed");
                });
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

    private void addToHistory(final String query) {
        queryHistory.searched(query);
    }

    public Observable<Collection<String>> searches() {
        return queryHistory.observe();
    }
}
