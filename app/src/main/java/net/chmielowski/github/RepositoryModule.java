package net.chmielowski.github;

import dagger.Module;
import dagger.Provides;

@Module
class RepositoryModule {
    private final String repository;

    RepositoryModule(final String repository) {
        this.repository = repository;
    }

    @RepositoryId
    @RepositoryScope
    @Provides
    String string() {
        return repository;
    }
}
