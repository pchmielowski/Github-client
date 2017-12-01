package net.chmielowski.github;

import io.reactivex.Single;

// TODO: rename to service
interface ReposRepository {
    Single<Repositories> fetchData();

    Single<Repositories.Item> repository(long repo);
}
