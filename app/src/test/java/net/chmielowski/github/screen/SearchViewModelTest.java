package net.chmielowski.github.screen;

import net.chmielowski.github.data.ReposRepository;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.Mockito.when;

public class SearchViewModelTest {
    @Test
    public void first() throws Exception {
        final ReposRepository service = Mockito.mock(ReposRepository.class);

        final String query = "query";

        when(service.items(SearchViewModel.Query.firstPage(query)))
                .thenReturn(Single.just(Collections.emptyList()));

        new SearchViewModel(service)
                .searchResults(Observable.just(query), Observable.never())
                .test()
                .assertValuesOnly(
                        ListState.initial(),
                        ListState.loading(),
                        ListState.loaded(Collections.emptyList())
                );
    }

}