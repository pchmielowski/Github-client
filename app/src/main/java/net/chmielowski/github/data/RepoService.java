package net.chmielowski.github.data;

import net.chmielowski.github.screen.SearchViewModel;

import java.lang.annotation.Retention;
import java.util.Collection;

import javax.inject.Qualifier;

import io.reactivex.Single;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public interface RepoService {
    // TODO: seperate methods: cacheItem, getCachedItem


    // TODO: return Maybe
    Single<Repositories.Item> item(String id);

    // TODO: return Maybe
    Single<Collection<Repositories.Item>> items(SearchViewModel.Query query);

    Single<Boolean> cacheItem(String name);

    @Qualifier
    @Retention(RUNTIME)
    @interface OnMainThread {
    }

    @Qualifier
    @Retention(RUNTIME)
    @interface Github {
    }
}
