package net.chmielowski.github;

public final class RepositoryViewModel {
    public final String name;

    RepositoryViewModel(final Repositories.Item repo) {
        this.name = repo.fullName;
    }
}
