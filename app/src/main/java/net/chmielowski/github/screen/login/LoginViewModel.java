package net.chmielowski.github.screen.login;

import android.databinding.ObservableField;

import net.chmielowski.github.data.LoginService;

import javax.inject.Inject;

import io.reactivex.Single;

public final class LoginViewModel {
    private final LoginService service;

    @Inject
    LoginViewModel(final LoginService service) {
        this.service = service;
    }

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> password = new ObservableField<>();

    public Single<Boolean> login() {
        return service.login(name.get(), password.get());
    }
}
