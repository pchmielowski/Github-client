package net.chmielowski.github.screen.search;

import net.chmielowski.github.data.NetworkState;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static net.chmielowski.github.screen.search.NetworkIndicatorViewModel.ViewState.DONT_SHOW;

@Singleton
public class NetworkIndicatorViewModel {

    private final NetworkState networkState;

    @Inject
    NetworkIndicatorViewModel(final NetworkState networkState) {
        this.networkState = networkState;
    }

    enum ViewState {
        OFFLINE, ONLINE, DONT_SHOW
    }

    Observable<ViewState> observe() {
        return networkState.observe()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(state -> {
                    switch (state) {
                        case OFFLINE:
                            return Observable.just(ViewState.OFFLINE);
                        case ONLINE:
                            return Observable.merge(
                                    Observable.just(ViewState.ONLINE),
                                    Observable.just(DONT_SHOW).delay(2, TimeUnit.SECONDS));
                        default:
                            throw new IllegalArgumentException(String.format("Unknown network state %s", state));
                    }
                });
    }
}
