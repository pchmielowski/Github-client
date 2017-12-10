package net.chmielowski.github.data;

import net.chmielowski.github.screen.SearchViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import io.reactivex.Single;

import static java.util.Collections.singletonList;
import static net.chmielowski.github.utils.TestUtils.sampleRepository;

public class GithubRepoServiceTest {
    private RestService rest;

    @Before
    public void setUp() throws Exception {
        rest = Mockito.mock(RestService.class);
    }

    @Test
    public void name() throws Exception {
        final Repositories repositories = new Repositories();
        repositories.items = singletonList(sampleRepository());
        final String query = "query";

        Mockito.when(rest.searchRepositories(query, 0))
                .thenReturn(Single.just(repositories));


        new GithubRepoService(rest, new HashMap<>())
                .items(SearchViewModel.Query.firstPage(query))
                .test()
                .assertValue(repositories.items);
    }
}