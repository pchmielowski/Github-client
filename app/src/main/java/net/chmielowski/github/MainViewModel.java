package net.chmielowski.github;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Single;

final class MainViewModel {
    private final ReposRepository repository;

    @Inject
    MainViewModel(final ReposRepository repository) {
        this.repository = repository;
    }

    Single<Collection<RepositoryViewModel>> fetchData() {
        return repository.fetchData()
                .map(repositories -> repositories.items.stream()
                        .map(RepositoryViewModel::new)
                        .collect(Collectors.toList()));
    }

    void onClicked(final Long id) {

    }
}
