package net.chmielowski.github;

import java.util.Collection;

import io.reactivex.Single;

// TODO: rename to service
public interface ReposRepository {
    Single<Collection<Repositories.Item>> fetchData(final String query);

    Single<Repositories.Item> repository(long repo);
}
