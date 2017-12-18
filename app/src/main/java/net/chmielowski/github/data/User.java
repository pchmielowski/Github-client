package net.chmielowski.github.data;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Credentials;

@Singleton
public final class User {
    @Inject
    User() { }

    @Nullable
    private String token;

    void logout() {
        token = null;
    }

    public void login(final String user, final String password) {
        token = Credentials.basic(user, password);
    }

    Optional<String> token() {
        Log.d("pchm", String.valueOf(token));
        return Optional.ofNullable(token);
    }
}
