package net.chmielowski.github.data;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.HashMap;

import io.reactivex.observers.TestObserver;

import static io.reactivex.Single.just;
import static java.util.Collections.singletonList;
import static net.chmielowski.github.TestUtils.sampleRepository;
import static net.chmielowski.github.screen.search.SearchViewModel.Query.firstPage;
import static okhttp3.MediaType.parse;
import static okhttp3.ResponseBody.create;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static retrofit2.Response.error;
import static retrofit2.Response.success;

public class WebRepositoryDataSourceTest {
    private Server rest;
    private RequestLimit limit;

    @Before
    public void setUp() throws Exception {
        rest = Mockito.mock(Server.class);
        limit = Mockito.mock(RequestLimit.class);
    }

    @Test
    public void fetchSuccessful() throws Exception {
        final Repositories repositories = new Repositories();
        final Repositories.Item repository = sampleRepository();
        repositories.items = singletonList(repository);

        final String query = "query";
        when(rest.find(query, 1))
                .thenReturn(just(success(repositories)));

        final WebRepositoryDataSource service = createDataSource();
        final TestObserver<Collection<Repositories.Item>> testObserver = service
                .repositories(firstPage(query))
                .test();

        testObserver.assertValue(repositories.items);
        assertThat(service.repositoryFromCache(repository.fullName), is(repository));
    }

    @Test
    public void fetchFailedWith500() throws Exception {
        final Repositories repositories = new Repositories();
        final Repositories.Item repository = sampleRepository();
        repositories.items = singletonList(repository);

        final String query = "query";
        when(rest.find(query, 1))
                .thenReturn(just(error(500, create(parse("text/plain"), "server error"))));

        final WebRepositoryDataSource service = createDataSource();
        final TestObserver<Collection<Repositories.Item>> testObserver = service
                .repositories(firstPage(query))
                .test();

        testObserver.assertNoValues();
        testObserver.assertComplete();
        try {
            service.repositoryFromCache(repository.fullName);
            fail("Exception not thrown");
        } catch (NullPointerException ignored) {
        }
    }

    @Test
    public void fetchFailedWith404() throws Exception {
        final Repositories repositories = new Repositories();
        final Repositories.Item repository = sampleRepository();
        repositories.items = singletonList(repository);

        final String query = "query";
        when(rest.find(query, 1))
                .thenReturn(just(error(404, create(parse("text/plain"), "client error"))));

        final WebRepositoryDataSource service = createDataSource();
        final TestObserver<Collection<Repositories.Item>> testObserver = service
                .repositories(firstPage(query))
                .test();

        testObserver.assertError(IllegalStateException.class);
    }

    @Test
    public void cacheItem() throws Exception {
        final Repositories.Item repo = sampleRepository();
        Mockito.when(rest.repository(repo.owner.login, repo.name))
                .thenReturn(just(success(repo)));

        final WebRepositoryDataSource service = createDataSource();

        service.cacheRepository(repo.fullName).subscribe();

        Assert.assertThat(service.repositoryFromCache(repo.fullName), notNullValue());
    }

    @NonNull
    private WebRepositoryDataSource createDataSource() {
        return new WebRepositoryDataSource(rest, new HashMap<>(), limit);
    }
}