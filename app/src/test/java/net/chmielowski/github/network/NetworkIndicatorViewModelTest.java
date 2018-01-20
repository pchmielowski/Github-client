package net.chmielowski.github.network;

import net.chmielowski.networkstate.BasicNetworkState;
import net.chmielowski.networkstate.NetworkIndicatorViewModel;
import net.chmielowski.networkstate.NetworkState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static io.reactivex.Completable.never;
import static net.chmielowski.networkstate.BasicNetworkState.State.OFFLINE;
import static net.chmielowski.networkstate.BasicNetworkState.State.ONLINE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class NetworkIndicatorViewModelTest {

    private final NetworkState state = Mockito.mock(NetworkState.class);
    private final Subject<BasicNetworkState.State> networkState = PublishSubject.create();
    private final CompletableSubject timer = CompletableSubject.create();

    @Before
    public void setUp() throws Exception {
        when(state.observe()).thenReturn(networkState);
    }

    @Test
    public void goesOffline() throws Exception {
        final NetworkIndicatorViewModel model = new NetworkIndicatorViewModel(state, never());

        model.start();
        assertThat(model.visible.get(), is(false));

        networkState.onNext(OFFLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(OFFLINE));
    }

    @Test
    public void returnsOnline() throws Exception {
        final NetworkIndicatorViewModel model = new NetworkIndicatorViewModel(state, never());

        model.start();

        networkState.onNext(OFFLINE);

        networkState.onNext(ONLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(ONLINE));
    }

    @Test
    public void timeout() throws Exception {
        final NetworkIndicatorViewModel model = new NetworkIndicatorViewModel(state, timer);

        model.start();

        networkState.onNext(OFFLINE);

        networkState.onNext(ONLINE);

        timer.onComplete();
        assertThat(model.visible.get(), is(false));
    }

    @Test
    public void goOfflineAgainBeforeTimeout() throws Exception {
        final NetworkIndicatorViewModel model = new NetworkIndicatorViewModel(state, timer);

        model.start();

        networkState.onNext(OFFLINE);

        networkState.onNext(ONLINE);

        networkState.onNext(OFFLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(OFFLINE));

        timer.onComplete();

        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(OFFLINE));
    }
}