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
    public ObservableBoolean visible = new ObservableBoolean(false);

    public ObservableField<BasicNetworkState.State> state = new ObservableField<>(ONLINE);

    private final NetworkState networkState;

    @Inject
    NetworkIndicatorViewModel(final NetworkState networkState) {
        this.networkState = networkState;
    }

    public Observable<BasicNetworkState.State> observe() {
        return networkState.observe()
                .doOnNext(state -> {
                    this.state.set(state);
                    switch (state) {
                        case ONLINE:
                            // TODO: cancel this task if network state changes before it was executed
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
