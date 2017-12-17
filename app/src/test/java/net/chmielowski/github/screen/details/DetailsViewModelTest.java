package net.chmielowski.github.screen.details;

import net.chmielowski.github.data.Favourites;
import net.chmielowski.github.data.RepoService;
import net.chmielowski.github.data.Repositories;
import net.chmielowski.github.screen.details.DetailsViewModel.Action;
import net.chmielowski.github.utils.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.observers.TestObserver;

import static net.chmielowski.github.screen.details.DetailsViewModel.Action.Type.LIKE;
import static org.mockito.Mockito.when;

public class DetailsViewModelTest {
    private RepoService service;
    private Favourites liked;

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(RepoService.class);
        liked = Mockito.mock(Favourites.class);
    }

    @Test
    public void addToFavourites() throws Exception {
        final Repositories.Item repo = TestUtils.sampleRepository();
        when(service.cached(repo.fullName)).thenReturn(repo);

        final DetailsViewModel model = new DetailsViewModel(service, liked, repo.fullName);
        final TestObserver<Action> testObserver = model.observeActions().test();

        model.toggleLike();

        testObserver.assertValue(new Action(LIKE, repo.name));
    }
}