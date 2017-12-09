package net.chmielowski.github.screen.search;

import android.databinding.ObservableBoolean;
import android.os.Handler;

import net.chmielowski.github.data.NetworkState;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public final class NetworkIndicatorViewModel {
    public ObservableBoolean visible = new ObservableBoolean(false);

    private final NetworkState networkState;

    @Inject
    NetworkIndicatorViewModel(final NetworkState networkState) {
        this.networkState = networkState;
    }

    Observable<NetworkState.State> observe() {
        return networkState.observe()
                .doOnNext(state -> {
                    switch (state) {
                        case ONLINE:
                            new Handler().postDelayed(() ->
                                    visible.set(false), 3000);
                            break;
                        case OFFLINE:
                            visible.set(true);
                            break;
                    }
                });
    }
}
