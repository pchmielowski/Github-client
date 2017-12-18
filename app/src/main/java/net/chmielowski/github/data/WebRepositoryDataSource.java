package net.chmielowski.github.data;

import android.support.annotation.NonNull;

import net.chmielowski.github.screen.SearchViewModel;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Response;

import static java.util.Objects.requireNonNull;

public final class WebRepositoryDataSource implements RepositoryDataSource {
    private final RestService service;
    private final Map<String, Repositories.Item> cache;

    @Inject
    WebRepositoryDataSource(final RestService service,
                            final Map<String, Repositories.Item> cache) {
        this.service = service;
        this.cache = cache;
    }

    @SuppressWarnings("ConstantConditions") // response.body() shouldn't be null if isSuccessful()
    @Override
    public Maybe<Collection<Repositories.Item>> repositories(final SearchViewModel.Query query) {
        // TODO: handle SocketTimeoutException
        return service.searchRepositories(query.text, query.page)
                .doOnSuccess(response -> {
                    if (response.code() / 100 == 4) {
                        throw new IllegalStateException("Response code is 4**");
                    }
                })
                .filter(Response::isSuccessful)
                .map(response -> response.body().items)
                .doOnSuccess(this::addToCache);
    }

    private void addToCache(final Collection<Repositories.Item> repositories) {
        repositories.forEach(item -> cache.put(item.fullName, item));
    }

    @Override
    @NonNull
    public Repositories.Item repositoryFromCache(final String name) {
        return requireNonNull(cache.get(name), String.format("Repository %s not repositoryFromCache!", name));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Single<Boolean> cacheRepository(final String name) {
        final String[] split = name.split("/");
        return cache.containsKey(name)
                ? Single.just(true)
                : service.repo(split[0], split[1])
                .doOnSuccess(response -> {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    final Repositories.Item item = response.body();
                    cache.put(item.fullName, item);
                })
                .map(Response::isSuccessful);
    }

}
