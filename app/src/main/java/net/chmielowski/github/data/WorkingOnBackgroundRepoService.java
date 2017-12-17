package net.chmielowski.github.data;

import net.chmielowski.github.screen.SearchViewModel;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class WorkingOnBackgroundRepoService implements RepoService {

    private final RepoService decorated;

    @Inject
    WorkingOnBackgroundRepoService(@Github final RepoService decorated) {
        this.decorated = decorated;
    }

    @Override
    public Repositories.Item cached(final String id) {
        return decorated.cached(id);
    }

    @Override
    public Maybe<Collection<Repositories.Item>> items(final SearchViewModel.Query query) {
        return decorated.items(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Boolean> cacheItem(final String name) {
        return decorated.cacheItem(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
