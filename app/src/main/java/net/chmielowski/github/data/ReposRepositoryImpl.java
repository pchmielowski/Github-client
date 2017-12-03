package net.chmielowski.github.data;

import android.support.annotation.NonNull;

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
    @NonNull
    public Single<Repositories.Item> item(final String name) {
        return Optional.ofNullable(cache.get(name))
                .map(Single::just)
                .orElseGet(() -> {
                    // TODO: just use owner/name
                    final String[] split = name.split("/");
                    return service.repo(split[0], split[1])
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                });
    }

}