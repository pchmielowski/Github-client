package net.chmielowski.github.network;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

public interface NetworkState {
    @NonNull
    <T> Observable<T> requireOnline(@NonNull Observable<T> observable);

    boolean isOnline();

    Observable<BasicNetworkState.State> observe();
}
