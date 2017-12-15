package net.chmielowski.github.data;

import net.chmielowski.github.screen.SearchViewModel;

import java.util.Collection;

import io.reactivex.Single;

public interface RepoService {
    // TODO: seperate methods: cacheItem, getCachedItem


    // TODO: return Maybe
    Single<Repositories.Item> item(String id);

    // TODO: return Maybe
    Single<Collection<Repositories.Item>> items(SearchViewModel.Query query);
}
