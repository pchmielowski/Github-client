package net.chmielowski.github.screen.list;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.RepositoryViewModel;

import java.util.stream.Collectors;

import javax.inject.Inject;

// TODO: SearchViewModel, SearchActivity
public final class ListViewModel {
    private final ReposRepository repository;
    private final ResultsView results;

    public final ObservableBoolean inputVisible = new ObservableBoolean(true);
    public final ObservableBoolean searchVisible = new ObservableBoolean(false);
    private String query;

    @Inject
    public ListViewModel(final ReposRepository repository, final ResultsView results) {
        this.repository = repository;
        this.results = results;
    }

    void onQueryChanged(final String query) {
        searchVisible.set(!query.isEmpty());
        this.query = query; // TODO: keep in Realm
    }

    void onScreenAppeared() {

    }

    public void onSearchClicked() {
        fetchData();
    }

    private void fetchData() {
        final String query = query();
        repository.items(query)
                .map(repositories -> repositories.stream()
                        .map(repo -> new RepositoryViewModel(repo, query))
                        .collect(Collectors.toList()))
                .doOnSuccess(__ -> searchVisible.set(false))
                .subscribe(results::update);
    }

    private String query() {
        return query; // if null, get from Realm
    }
}
