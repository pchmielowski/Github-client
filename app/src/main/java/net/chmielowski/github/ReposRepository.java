package net.chmielowski.github;

import io.reactivex.Single;

interface ReposRepository {
    Single<Repositories> fetchData();
}
