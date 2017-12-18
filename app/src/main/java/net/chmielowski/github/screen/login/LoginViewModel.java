package net.chmielowski.github.screen.login;

import android.databinding.ObservableField;
import android.util.Log;

import net.chmielowski.github.data.User;

import javax.inject.Inject;

import io.reactivex.Single;

public final class LoginViewModel {
    private final User user;

    @Inject
    LoginViewModel(final User user) {
        this.user = user;
    }

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> password = new ObservableField<>();

    public Single<Boolean> login() {
        Log.d("pchm", "LoginViewModel login ");
        user.login(name.get(), password.get());
        Log.d("pchm", "saved");
        return Single.just(true)
                .doOnSuccess(__ -> Log.d("pchm", "Completed"));
    }
}
