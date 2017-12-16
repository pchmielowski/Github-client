package net.chmielowski.github.network;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Handler;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

import static net.chmielowski.github.network.BasicNetworkState.State.ONLINE;

@Singleton
public final class NetworkIndicatorViewModel {
    private final Handler handler = new Handler();
    private final int delayMillis;
    public ObservableBoolean visible = new ObservableBoolean(false);
    public ObservableField<BasicNetworkState.State> state = new ObservableField<>(ONLINE);

    private final Runnable hide = () -> visible.set(false);

    private final NetworkState networkState;

    private NetworkIndicatorViewModel(final int delayMillis, final NetworkState networkState) {
        this.delayMillis = delayMillis;
        this.networkState = networkState;
    }

    @Inject
    NetworkIndicatorViewModel(final NetworkState networkState) {
        this(3000, networkState);
    }

    public Observable<BasicNetworkState.State> observe() {
        return networkState.observe()
                .doOnNext(state -> {
                    this.state.set(state);
                    switch (state) {
                        case ONLINE:
                            handler.postDelayed(hide, delayMillis);
                            break;
                        case OFFLINE:
                            handler.removeCallbacks(hide);
                            visible.set(true);
                            break;
                    }
                });
    }
}
