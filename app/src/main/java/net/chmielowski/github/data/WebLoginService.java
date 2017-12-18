package net.chmielowski.github.data;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

final class WebLoginService implements LoginService {
    private final User user;
    private final Server server;

    @Inject
    WebLoginService(final User user, final Server server) {
        this.user = user;
        this.server = server;
    }

    @Override
    public Single<Boolean> login(final String name, final String password) {
        user.login(name, password);
        return server.root()

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .map(Response::isSuccessful)
                .doOnSuccess(isSuccessful -> {
                    if (!isSuccessful) {
                        user.logout();
                    }
                });
    }
}
