package net.chmielowski.github.screen.fav;

import net.chmielowski.github.data.Persistence;
import net.chmielowski.github.data.RepoService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.SingleSubject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class FavsViewModelTest {

    private Persistence db;
    private RepoService service;

    @Before
    public void setUp() throws Exception {
        db = Mockito.mock(Persistence.class);
        service = Mockito.mock(RepoService.class);
    }

    @Test
    public void itemCachedWithSuccess() throws Exception {
        itemCached(true);
    }

    @Test
    public void itemCachedWithFailure() throws Exception {
        itemCached(false);
    }

    private void itemCached(final boolean result) {
        final String repository = "repo";
        final SingleSubject<Boolean> subject = SingleSubject.create();
        when(service.cacheItem(repository))
                .thenReturn(subject);

        final FavsViewModel model = new FavsViewModel(db, service);
        final TestObserver<Boolean> testObserver = model
                .cache(repository)
                .test();
        assertThat(model.loading.get(), is(true));

        subject.onSuccess(result);
        testObserver.assertValue(result);
        assertThat(model.loading.get(), is(false));
    }
}