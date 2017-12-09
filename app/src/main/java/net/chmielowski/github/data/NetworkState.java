package net.chmielowski.github.data;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.Observable;

public final class NetworkState {
    private final ConnectivityManager manager;

    @Inject
    NetworkState(final ConnectivityManager manager) {
        this.manager = manager;
    }

    @NonNull
    public <T> Observable<T> requireOnline(@NonNull final Observable<T> observable) {
        return isOnline() ? observable : Observable.empty();
    }

    private boolean isOnline() {
        return Optional.ofNullable(manager.getActiveNetworkInfo())
                .map(NetworkInfo::isConnected)
                .orElse(false);
    }
}
