package net.chmielowski.github;

public final class RepositoryViewModel {
    public final String name;
    public final long id;

    RepositoryViewModel(final Repositories.Item repo) {
        this.name = repo.fullName;
        this.id = repo.id;
    }
}
