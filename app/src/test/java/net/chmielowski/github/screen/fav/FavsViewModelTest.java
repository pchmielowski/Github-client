package net.chmielowski.github.screen.fav;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import net.chmielowski.github.data.Favourites;
import net.chmielowski.github.data.RepositoryDataSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.SingleSubject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class FavsViewModelTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private RepositoryDataSource service;
    private Favourites favourites;

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(RepositoryDataSource.class);
        favourites = Mockito.mock(Favourites.class);
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
        final String repository = "repository";
        final SingleSubject<Boolean> subject = SingleSubject.create();
        when(service.cacheRepository(repository))
                .thenReturn(subject);

        final FavsViewModel model = new FavsViewModel(service, favourites);
        final TestObserver<Boolean> testObserver = model
                .cache(repository)
                .test();
        assertThat(model.loading.get(), is(true));

        subject.onSuccess(result);
        testObserver.assertValue(result);
        assertThat(model.loading.get(), is(false));
    }
}