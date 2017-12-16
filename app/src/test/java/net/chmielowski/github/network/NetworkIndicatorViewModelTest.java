package net.chmielowski.github.network;

import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static net.chmielowski.github.network.BasicNetworkState.State.OFFLINE;
import static net.chmielowski.github.network.BasicNetworkState.State.ONLINE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class NetworkIndicatorViewModelTest {

    @Test
    public void goOfflineAndOnlineAgain() throws Exception {
        final NetworkState state = Mockito.mock(NetworkState.class);
        final Subject<BasicNetworkState.State> networkState = PublishSubject.create();
        when(state.observe()).thenReturn(networkState);

        final CompletableSubject timer = CompletableSubject.create();

        final NetworkIndicatorViewModel model = new NetworkIndicatorViewModel(state, timer);

        model.start();
        assertThat(model.visible.get(), is(false));

        networkState.onNext(OFFLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(OFFLINE));

        networkState.onNext(ONLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(ONLINE));

        timer.onComplete();
        assertThat(model.visible.get(), is(false));
    }

    @Test
    public void goOfflineAndOnlineAndOfflineBeforeTimerCompletes() throws Exception {
        final NetworkState state = Mockito.mock(NetworkState.class);
        final Subject<BasicNetworkState.State> networkState = PublishSubject.create();
        when(state.observe()).thenReturn(networkState);

        final CompletableSubject timer = CompletableSubject.create();

        final NetworkIndicatorViewModel model = new NetworkIndicatorViewModel(state, timer);

        model.start();
        assertThat(model.visible.get(), is(false));

        networkState.onNext(OFFLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(OFFLINE));

        networkState.onNext(ONLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(ONLINE));

        networkState.onNext(OFFLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(OFFLINE));

        timer.onComplete();

        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(OFFLINE));
    }
}