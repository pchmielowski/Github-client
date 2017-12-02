package net.chmielowski.github.screen.list;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.RepositoryViewModel;

import java.util.stream.Collectors;

import javax.inject.Inject;

public final class ListViewModel {
    private final ReposRepository repository;
    private final Adapter adapter; // TODO: RepositoriesView

    public final ObservableBoolean searchVisible = new ObservableBoolean(false);
    public final ObservableBoolean fabVisible = new ObservableBoolean(false);
    private String query;

    public ListViewModel(final ReposRepository repository, final Adapter adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }

    void onTextEntered(final String text) {
        fabVisible.set(!text.isEmpty());
        query = text; // TODO: keep in Realm
    }

    void onScreenAppeared() {
//        fetchData();
    }

    void fetchData() {
        repository.fetchData(query())
                .map(repositories -> repositories.stream()
                        .map(RepositoryViewModel::new)
                        .collect(Collectors.toList()))
                .subscribe(adapter::update);
    }

    private String query() {
        return query; // if null, get from Realm
    }

    public void fabClicked() {
        fetchData();
    }
}
