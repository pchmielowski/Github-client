package net.chmielowski.github;

import javax.inject.Inject;

final class MainViewModel {
    private final ReposRepository repository;

    @Inject
    MainViewModel(final ReposRepository repository) {
        this.repository = repository;

        repository.fetchData();
    }
}
