package net.chmielowski.github;

import android.util.Log;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

final class ReposRepositoryImpl implements ReposRepository {
    private final RestService service;

    @Inject
    ReposRepositoryImpl(final RestService service) {
        this.service = service;
    }

    @Override
    public void fetchData() {
        service.searchRepositories("java")
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Repositories>() {
                    @Override
                    public void accept(final Repositories repositories) throws Exception {
                        Log.d("pchm", String.valueOf(repositories.total_count));
                    }
                });
    }
}
