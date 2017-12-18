package net.chmielowski.github.data;

import android.support.annotation.NonNull;

import net.chmielowski.github.screen.search.SearchViewModel;

import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Response;

import static java.util.Objects.requireNonNull;

public final class WebRepositoryDataSource implements RepositoryDataSource {
    private final Server server;
    private final Map<String, Repositories.Item> cache;

    @Inject
    WebRepositoryDataSource(final Server server,
                            final Map<String, Repositories.Item> cache) {
        this.server = server;
        this.cache = cache;
    }

    @SuppressWarnings("ConstantConditions") // response.body() shouldn't be null if isSuccessful()
    @Override
    public Maybe<Collection<Repositories.Item>> repositories(final SearchViewModel.Query query) {
        return makeRequest(server.find(query.text, query.page))
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
                : makeRequest(server.repository(split[0], split[1]))
                .doOnSuccess(response -> {
                    final Repositories.Item item = response.body();
                    cache.put(item.fullName, item);
                })
                .map(__ -> true)
                .toSingle(false);
    }

    /**
     * Executes request and returns {@link Maybe}
     * - with the value if response is successful
     * - without value if response code is 5**, 6** or {@link SocketTimeoutException} was thrown
     *
     * @throws IllegalStateException if response code is 4** (but not 403 which can happen)
     *                               Also rethrows any {@link Throwable} whis is not {@link SocketTimeoutException}
     */
    private <T> Maybe<Response<T>> makeRequest(final Single<Response<T>> request) {
        return request
                .doOnSuccess(response -> {
                    final int code = response.code();
                    if (code == 403) {
                        return;
                    }
                    if (code / 100 == 4) {
                        throw new IllegalStateException("Response code is 4**");
                    }
                })
                .filter(Response::isSuccessful)
                .onErrorComplete(e -> e instanceof SocketTimeoutException);

    }

}
