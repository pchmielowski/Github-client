package net.chmielowski.github.data;

import net.chmielowski.github.screen.SearchViewModel;

import java.lang.annotation.Retention;
import java.util.Collection;

import javax.inject.Qualifier;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public interface RepositoryDataSource {
    Maybe<Collection<Repositories.Item>> repositories(SearchViewModel.Query query);

    Single<Boolean> cacheRepository(String name);

    Repositories.Item repositoryFromCache(String name);

    @Qualifier
    @Retention(RUNTIME)
    @interface WorkOnBackground {
    }

    @Qualifier
    @Retention(RUNTIME)
    @interface Github {
    }
}
