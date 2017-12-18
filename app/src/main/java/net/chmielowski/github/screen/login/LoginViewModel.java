package net.chmielowski.github.screen.login;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import net.chmielowski.github.data.LoginService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public final class LoginViewModel {
    private final LoginService service;

    public ObservableBoolean loading = new ObservableBoolean(false);

    @Inject
    LoginViewModel(final LoginService service) {
        this.service = service;
    }

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> password = new ObservableField<>();

    public Single<Boolean> login() {
        return service.login(name.get(), password.get())
                .doOnSubscribe(__ -> loading.set(true))
                .doOnSuccess(__ -> loading.set(false));
    }
}
