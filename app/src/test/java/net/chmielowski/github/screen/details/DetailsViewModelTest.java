package net.chmielowski.github.screen.details;

import net.chmielowski.github.data.Favourites;
import net.chmielowski.github.data.RepoService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DetailsViewModelTest {
    private RepoService service;
    private Favourites liked;

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(RepoService.class);
    }

    @Test
    public void addToFavourites() throws Exception {
        new DetailsViewModel(service, liked, "repo");
    }
}