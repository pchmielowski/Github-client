package net.chmielowski.github.screen.fav;

import net.chmielowski.github.data.Persistence;
import net.chmielowski.github.data.RepoService;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.subjects.SingleSubject;

import static org.hamcrest.CoreMatchers.is;

public class FavsViewModelTest {
    @Test
    public void name() throws Exception {
        final Persistence db = Mockito.mock(Persistence.class);
        final RepoService service = Mockito.mock(RepoService.class);

        final SingleSubject<Boolean> subject = SingleSubject.create();
        Mockito.when(service.cacheItem("repo"))
                .thenReturn(subject);

        final FavsViewModel model = new FavsViewModel(db, service);

        model.cache("repo")
                .subscribe();

        Assert.assertThat(model.loading.get(), is(true));
        subject.onSuccess(true);
        Assert.assertThat(model.loading.get(), is(false));
    }
}