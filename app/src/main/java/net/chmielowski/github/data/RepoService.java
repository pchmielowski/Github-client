package net.chmielowski.github.data;

import net.chmielowski.github.screen.SearchViewModel;

import java.lang.annotation.Retention;
import java.util.Collection;

import javax.inject.Qualifier;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

// TODO: DataSource
public interface RepoService {
    Repositories.Item cached(String id);

    Maybe<Collection<Repositories.Item>> items(SearchViewModel.Query query);

    Single<Boolean> cacheItem(String name);

    @Qualifier
    @Retention(RUNTIME)
    @interface WorkOnBackground {
    }

    @Qualifier
    @Retention(RUNTIME)
    @interface Github {
    }
}
