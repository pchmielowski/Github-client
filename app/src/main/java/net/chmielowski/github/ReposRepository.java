package net.chmielowski.github;

import java.util.Collection;

import io.reactivex.Single;

// TODO: rename to service
public interface ReposRepository {
    Single<Collection<Repositories.Item>> items(final String query);

    Single<Repositories.Item> item(long id);
}
