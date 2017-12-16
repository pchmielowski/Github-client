package net.chmielowski.github.network;

import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static net.chmielowski.github.network.BasicNetworkState.State.OFFLINE;
import static net.chmielowski.github.network.BasicNetworkState.State.ONLINE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NetworkIndicatorViewModelTest {
    private Subject<BasicNetworkState.State> subject = PublishSubject.create();

    @Test
    public void goOfflineAndOnlineAgain() throws Exception {
        final NetworkState state = Mockito.mock(NetworkState.class);
        final NetworkIndicatorViewModel model = new NetworkIndicatorViewModel(0, state);

        Mockito.when(state.observe())
                .thenReturn(subject);

        model.start();

        assertThat(model.visible.get(), is(false));

        subject.onNext(OFFLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(OFFLINE));

        subject.onNext(ONLINE);
        assertThat(model.visible.get(), is(true));
        assertThat(model.state.get(), is(ONLINE));
    }
}