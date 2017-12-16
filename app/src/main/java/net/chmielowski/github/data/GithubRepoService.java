package net.chmielowski.github.data;

import android.support.annotation.NonNull;

import net.chmielowski.github.screen.SearchViewModel;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static java.util.Objects.requireNonNull;

public final class GithubRepoService implements RepoService {
    private final RestService service;
    private final Map<String, Repositories.Item> cache;

    @Inject
    GithubRepoService(final RestService service,
                      final Map<String, Repositories.Item> cache) {
        this.service = service;
        this.cache = cache;
    }

    @Override
    public Single<Collection<Repositories.Item>> items(final SearchViewModel.Query query) {
        return service.searchRepositories(query.text, query.page)
                .map(repositories ->
                        repositories.items)
                .doOnSuccess(repositories -> repositories
                        .forEach(item -> {
                            cache.put(item.fullName, item);
                        }))
                .subscribeOn(Schedulers.io());
    }

    @Override
    @NonNull
    public Repositories.Item cached(final String name) {
        return requireNonNull(cache.get(name), String.format("Repository %s not cached!", name));
    }

    @Override
    public Single<Boolean> cacheItem(final String name) {
        final String[] split = name.split("/");
        return cache.containsKey(name)
                ? Single.just(true)
                : service.repo(split[0], split[1])
                .subscribeOn(Schedulers.io())
                .map(Response::isSuccessful);
    }

}
