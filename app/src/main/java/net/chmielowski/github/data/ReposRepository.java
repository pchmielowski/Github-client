package net.chmielowski.github.data;

import java.util.Collection;

import io.reactivex.Single;

// TODO: rename to service
public interface ReposRepository {
    Single<Collection<Repositories.Item>> items(final String query);

    Repositories.Item item(String id);
}
