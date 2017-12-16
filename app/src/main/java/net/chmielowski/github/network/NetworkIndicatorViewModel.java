package net.chmielowski.github.network;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static io.reactivex.Completable.timer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.chmielowski.github.network.BasicNetworkState.State.ONLINE;

@Singleton
public final class NetworkIndicatorViewModel {
    private final int delay;
    public ObservableBoolean visible = new ObservableBoolean(false);
    public ObservableField<BasicNetworkState.State> state = new ObservableField<>(ONLINE);

    private final NetworkState networkState;

    @Nullable
    private Disposable disposable;

    private NetworkIndicatorViewModel(final int delay, final NetworkState networkState) {
        this.delay = delay;
        this.networkState = networkState;
    }

    @Inject
    NetworkIndicatorViewModel(final NetworkState networkState) {
        this(3, networkState);
    }

    public Observable<BasicNetworkState.State> observe() {
        return networkState.observe()
                .doOnNext(state -> {
                    this.state.set(state);
                    switch (state) {
                        case ONLINE:
                            if (disposable != null) {
                                throw new IllegalStateException(
                                        "Went online without going offline before"
                                );
                            }
                            disposable = timer(delay, SECONDS).subscribe(() -> visible.set(false));
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
