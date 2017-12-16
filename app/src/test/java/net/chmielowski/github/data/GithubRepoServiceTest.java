package net.chmielowski.github.data;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.HashMap;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import retrofit2.Response;

import static java.util.Collections.singletonList;
import static net.chmielowski.github.screen.SearchViewModel.Query.firstPage;
import static net.chmielowski.github.utils.TestUtils.sampleRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class GithubRepoServiceTest {
    private RestService rest;

    @Before
    public void setUp() throws Exception {
        rest = Mockito.mock(RestService.class);
    }

    @Test
    public void fetchSuccessful() throws Exception {
        final Repositories repositories = new Repositories();
        final Repositories.Item repository = sampleRepository();
        repositories.items = singletonList(repository);

        final String query = "query";
        when(rest.searchRepositories(query, 0))
                .thenReturn(Single.just(Response.success(repositories)));

        final GithubRepoService service = new GithubRepoService(rest, new HashMap<>());
        final TestObserver<Collection<Repositories.Item>> testObserver = service
                .items(firstPage(query))
                .test();

        testObserver.assertValue(repositories.items);
        assertThat(service.cached(repository.fullName), is(repository));
    }
}