package net.chmielowski.github.data;

import net.chmielowski.github.Github;
import net.chmielowski.github.screen.SearchViewModel;

import java.util.Collection;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public final class MainThreadRepoServiceDecorator implements RepoService {

    private final RepoService decorated;

    @Inject
    MainThreadRepoServiceDecorator(@Github final RepoService decorated) {
        this.decorated = decorated;
    }

    @Override
    public Single<Repositories.Item> item(final String id) {
        return decorated.item(id)
                .compose(MainThreadRepoServiceDecorator::onMainThread);
    }

    @Override
    public Single<Collection<Repositories.Item>> items(final SearchViewModel.Query query) {
        return decorated.items(query)
                .compose(MainThreadRepoServiceDecorator::onMainThread);
    }

    @Override
    public Single<Boolean> cacheItem(final String name) {
        return decorated.cacheItem(name)
                .compose(MainThreadRepoServiceDecorator::onMainThread);
    }

    private static <T> Single<T> onMainThread(final Single<T> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread());
    }

}
