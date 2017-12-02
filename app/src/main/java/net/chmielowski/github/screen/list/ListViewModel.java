package net.chmielowski.github.screen.list;

import android.databinding.ObservableBoolean;

import net.chmielowski.github.ReposRepository;
import net.chmielowski.github.RepositoryViewModel;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Single;

public final class ListViewModel {
    private final ReposRepository repository;

    public final ObservableBoolean searchVisible = new ObservableBoolean(false);

    @Inject
    ListViewModel(final ReposRepository repository) {
        this.repository = repository;
    }

    Single<Collection<RepositoryViewModel>> fetchData(final String text) {
        return repository.fetchData(text)
                .map(repositories -> repositories.stream()
                        .map(RepositoryViewModel::new)
                        .collect(Collectors.toList()));
    }

    public void fabClicked() {
        searchVisible.set(true);
    }

}
