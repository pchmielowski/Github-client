package net.chmielowski.github.data;

import android.support.annotation.Nullable;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Credentials;

@Singleton
public final class User {
    @Inject
    public User() { }

    @Nullable
    private String token;

    public void logout() {
        token = null;
    }

    public void login(final String user, final String password) {
        token = Credentials.basic(user, password);
    }

    public Optional<String> token() {
        return Optional.ofNullable(token);
    }
}
