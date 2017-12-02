package net.chmielowski.github.screen.list;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.RepositoryViewModel;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.realm.Realm;

// TODO: SearchViewModel, SearchActivity
public final class ListViewModel {
    private final ReposRepository repository;
    private final ResultsView results;

    public final ObservableBoolean inputVisible = new ObservableBoolean(true);
    public final ObservableBoolean searchVisible = new ObservableBoolean(false);
    @Nullable
    private String query;

    @Inject
    public ListViewModel(final ReposRepository repository, final ResultsView results) {
        this.repository = repository;
        this.results = results;
    }

    void onQueryChanged(final String query) {
        searchVisible.set(!query.isEmpty());
        this.query = query;

        // TODO: move to another class
        //noinspection ConstantConditions
        Realm.getInstance(Realm.getDefaultConfiguration())
                .executeTransaction(realm ->
                        realm.copyToRealmOrUpdate(RealmString.from(query)));
    }

    void onScreenAppeared() {
        fetchData();
    }

    public void onSearchClicked() {
        fetchData();
    }

    private void fetchData() {
        query().ifPresent(query ->
                repository.items(query)
                        .map(repositories -> repositories.stream()
                                .map(repo -> new RepositoryViewModel(repo, query))
                                .collect(Collectors.toList()))
                        .doOnSuccess(__ -> searchVisible.set(false))
                        .subscribe(results::update));
    }

    @NonNull
    private Optional<String> query() {
        if (query != null) {
            return Optional.of(query);
        }
        //noinspection ConstantConditions
        final RealmString first = Realm.getInstance(Realm.getDefaultConfiguration())
                .where(RealmString.class)
                .findFirst();
        return Optional.ofNullable(first)
                .map(it -> it.value);
    }
}
