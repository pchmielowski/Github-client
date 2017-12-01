package net.chmielowski.github;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

final class ReposRepositoryImpl implements ReposRepository {
    private final RestService service;

    @Inject
    ReposRepositoryImpl(final RestService service) {
        this.service = service;
    }

    @Override
    public Single<Repositories> fetchData() {
        return service.searchRepositories("java")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
