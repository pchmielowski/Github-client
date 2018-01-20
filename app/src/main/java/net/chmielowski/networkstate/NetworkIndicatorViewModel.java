package net.chmielowski.networkstate;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

import static io.reactivex.Completable.timer;
import static java.util.concurrent.TimeUnit.SECONDS;

public final class NetworkIndicatorViewModel {
    private final Completable timer;
    public ObservableBoolean visible = new ObservableBoolean(false);
    public ObservableField<BasicNetworkState.State> state = new ObservableField<>(BasicNetworkState.State.ONLINE);

    private final NetworkState networkState;

    @Nullable
    private Disposable disposable;

    public NetworkIndicatorViewModel(final NetworkState networkState, final Completable timer) {
        this.networkState = networkState;
        this.timer = timer;
    }

    public NetworkIndicatorViewModel(final NetworkState networkState) {
        this(networkState, timer(3, SECONDS));
    }

    public Disposable start() {
        return networkState.observe()
                .subscribe(state -> {
                    this.state.set(state);
                    switch (state) {
                        case ONLINE:
                            if (disposable != null) {
                                throw new IllegalStateException(
                                        "Went online without going offline before"
                                );
                            }
                            disposable = timer.subscribe(() -> visible.set(false));
                            break;
                        case OFFLINE:
                            if (disposable != null) {
                                disposable.dispose();
                                disposable = null;
                            }
                            visible.set(true);
                            break;
                    }
                });
    }
}
