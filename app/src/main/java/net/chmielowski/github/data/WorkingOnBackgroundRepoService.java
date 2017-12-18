package net.chmielowski.github.data;

import net.chmielowski.github.screen.SearchViewModel;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class WorkingOnBackgroundRepoService implements RepositoryDataSource {

    private final RepositoryDataSource decorated;

    @Inject
    WorkingOnBackgroundRepoService(@Github final RepositoryDataSource decorated) {
        this.decorated = decorated;
    }

    @Override
    public Repositories.Item repositoryFromCache(final String name) {
        return decorated.repositoryFromCache(name);
    }

    @Override
    public Maybe<Collection<Repositories.Item>> repositories(final SearchViewModel.Query query) {
        return decorated.repositories(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Boolean> cacheRepository(final String name) {
        return decorated.cacheRepository(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
