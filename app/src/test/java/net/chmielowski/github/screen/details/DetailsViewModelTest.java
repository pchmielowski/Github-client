package net.chmielowski.github.screen.details;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import net.chmielowski.github.TestUtils;
import net.chmielowski.github.data.Favourites;
import net.chmielowski.github.data.Repositories;
import net.chmielowski.github.data.RepositoryDataSource;
import net.chmielowski.github.screen.details.DetailsViewModel.Action;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import io.reactivex.observers.TestObserver;

import static io.reactivex.Single.just;
import static net.chmielowski.github.screen.details.DetailsViewModel.Action.Type.LIKE;
import static net.chmielowski.github.screen.details.DetailsViewModel.Action.Type.UNLIKE;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

public class DetailsViewModelTest {
    @Rule

    public TestRule rule = new InstantTaskExecutorRule();
    @Test
    public void addToFavourites() throws Exception {
        toggleLike(true, LIKE);
    }

    @Test
    public void removeFromFavourites() throws Exception {
        toggleLike(false, UNLIKE);
    }

    private void toggleLike(final boolean becomeLiked, final Action.Type expectedAction) {
        final RepositoryDataSource service = Mockito.mock(RepositoryDataSource.class);
        final Favourites liked = Mockito.mock(Favourites.class);

        final Repositories.Item repo = TestUtils.sampleRepository();
        when(service.repositoryFromCache(repo.fullName)).thenReturn(repo);
        when(liked.toggle(repo)).thenReturn(just(becomeLiked));

        final DetailsViewModel model = new DetailsViewModel(service, liked, repo.fullName);

        model.toggleLike();

        Assert.assertThat(model.addedLiveData.getValue(), is(new Action(expectedAction, repo.name)));
    }
}