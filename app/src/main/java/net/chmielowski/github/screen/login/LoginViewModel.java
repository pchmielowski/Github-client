package net.chmielowski.github.screen.login;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import net.chmielowski.github.data.LoginService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@AutoFactory
public final class LoginViewModel extends ViewModel {
    private final LoginService service;

    public ObservableBoolean loading = new ObservableBoolean(false);

    LoginViewModel(@Provided final LoginService service) {
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
