package net.chmielowski.github.screen;

import net.chmielowski.github.data.ReposRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import io.reactivex.Observable;

import static io.reactivex.Single.just;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static net.chmielowski.github.screen.ListState.initial;
import static net.chmielowski.github.screen.ListState.loaded;
import static net.chmielowski.github.screen.ListState.loading;
import static net.chmielowski.github.utils.TestUtils.sampleRepository;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class SearchViewModelTest {

    private ReposRepository service;

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(ReposRepository.class);
    }

    @Test
    public void justInitialValueOnZeroUserActions() throws Exception {
        new SearchViewModel(service)
                .searchResults(Observable.never(), Observable.never())
                .test()
                .assertValuesOnly(initial());

        verifyZeroInteractions(service);
    }

    @Test
    public void serviceReturnsEmptyList() throws Exception {
        final String query = "query";

        when(service.items(SearchViewModel.Query.firstPage(query)))
                .thenReturn(just(emptyList()));

        new SearchViewModel(service)
                .searchResults(Observable.just(query), Observable.never())
                .test()
                .assertValuesOnly(
                        initial(),
                        loading(),
                        loaded(emptyList())
                );
    }

    @Test
    public void serviceReturnsOneItem() throws Exception {
        final String query = "query";

        when(service.items(SearchViewModel.Query.firstPage(query)))
                .thenReturn(just(singletonList(sampleRepository())));

        new SearchViewModel(service)
                .searchResults(Observable.just(query), Observable.never())
                .test()
                .assertValuesOnly(
                        initial(),
                        loading(),
                        loaded(singletonList(new RepositoryViewModel(sampleRepository(), query)))
                );
    }

}