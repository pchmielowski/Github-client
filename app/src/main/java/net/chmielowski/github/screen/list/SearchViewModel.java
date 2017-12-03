package net.chmielowski.github.screen.list;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.RepositoryViewModel;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public final class SearchViewModel {
    private final ReposRepository repository;

    private final Subject<CharSequence> querySubject = PublishSubject.create();
    private final Subject<Object> searchSubject = PublishSubject.create();

    public final ObservableBoolean inputVisible = new ObservableBoolean(true);
    public final ObservableBoolean searchVisible = new ObservableBoolean(false);
    public final ObservableBoolean loading = new ObservableBoolean(false);

    @Inject
    SearchViewModel(final ReposRepository repository) {
        this.repository = repository;
        queryAsString()
                .doOnComplete(() -> {
                    throw new IllegalStateException("querySubject completed");
                })
                .subscribe(query -> searchVisible.set(!query.isEmpty()));
    }

    private Observable<String> queryAsString() {
        return this.querySubject
                .map(String::valueOf);
    }

    Observer<CharSequence> queryChanged() {
        return querySubject;
    }

    Observer<Object> searchClicked() {
        return searchSubject;
    }

    Observable<Collection<RepositoryViewModel>> searchResults() {
        return searchSubject.withLatestFrom(queryAsString(), (__, s) -> s)
                .flatMapSingle(query ->
                        repository.items(query)
                                .map(repositories -> repositories.stream()
                                        .map(repo -> new RepositoryViewModel(repo, query))
                                        .collect(Collectors.toList()))
                                .doOnSubscribe(__ -> loading.set(true))
                                .doOnSuccess(__ -> loading.set(false))
                                .doOnSuccess(__ -> searchVisible.set(false)));
    }
}
