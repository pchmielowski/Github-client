package net.chmielowski.github.data;

import android.util.LongSparseArray;
import android.util.SparseArray;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class ReposRepositoryImpl implements ReposRepository {
    private final RestService service;
    private final Map<String, Repositories.Item> cache;

    @Inject
    ReposRepositoryImpl(final RestService service,
                        final Map<String, Repositories.Item> cache) {
        this.service = service;
        this.cache = cache;
    }

    @Override
    public Single<Collection<Repositories.Item>> items(final String query) {
        return service.searchRepositories(query)
                .map(repositories ->
                        repositories.items)
                .doOnSuccess(repositories -> repositories
                        .forEach(item -> {
                            cache.put(item.fullName, item);
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Repositories.Item item(final String name) {
        return Optional.ofNullable(cache.get(name))
                .orElseGet(() -> fetch(name));
    }

    private Repositories.Item fetch(String id) {
        // TODO: fetch from REST API
        return null;
    }
}
