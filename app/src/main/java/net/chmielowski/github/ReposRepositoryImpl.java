package net.chmielowski.github;

import android.util.LongSparseArray;

import net.chmielowski.github.screen.fav.RealmRepo;
import net.chmielowski.github.screen.list.Cache;

import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

final class ReposRepositoryImpl implements ReposRepository {
    private final RestService service;
    private final LongSparseArray<Repositories.Item> cache;
    private final Cache realmCache;

    @Inject
    ReposRepositoryImpl(final RestService service,
                        final LongSparseArray<Repositories.Item> cache,
                        final Cache realmCache) {
        this.service = service;
        this.cache = cache;
        this.realmCache = realmCache;
    }

    @Override
    public Single<Collection<Repositories.Item>> items(final String query) {
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
    public Repositories.Item item(final long id) {
        return Optional.ofNullable(cache.get(id))
                .orElseGet(() -> realmCache.get(id));
    }
}
