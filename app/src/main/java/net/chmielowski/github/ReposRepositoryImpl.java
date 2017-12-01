package net.chmielowski.github;

import android.util.LongSparseArray;

import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

final class ReposRepositoryImpl implements ReposRepository {
    private final RestService service;
    private final LongSparseArray<Repositories.Item> cache;

    @Inject
    ReposRepositoryImpl(final RestService service, final LongSparseArray<Repositories.Item> cache) {
        this.service = service;
        this.cache = cache;
    }

    @Override
    public Single<Collection<Repositories.Item>> fetchData(final String query) {
        return service.searchRepositories(query)
                .map(repositories ->
                        repositories.items)
                .doOnSuccess(repositories -> repositories
                        .forEach(item -> {
                            cache.put(item.id, item);
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Repositories.Item> repository(final long repo) {
        //noinspection ConstantConditions
        return Optional.ofNullable(cache.get(repo)).map(Single::just).get(); // TODO: fetch data
    }
}
