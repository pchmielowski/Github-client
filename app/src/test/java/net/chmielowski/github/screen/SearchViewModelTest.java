package net.chmielowski.github.screen;

import android.support.annotation.NonNull;

import net.chmielowski.github.data.ReposRepository;
import net.chmielowski.github.data.Repositories;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.Single;

import static io.reactivex.Single.just;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
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
                .assertValuesOnly(ListState.initial());

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
                        ListState.initial(),
                        ListState.loading(),
                        ListState.loaded(emptyList())
                );
    }

    @Ignore("RepositoryViewModel should be Android independent")
    @Test
    public void second() throws Exception {
        final String query = "query";

        when(service.items(SearchViewModel.Query.firstPage(query)))
                .thenReturn(just(singletonList(sampleRepository())));

        new SearchViewModel(service)
                .searchResults(Observable.just(query), Observable.never())
                .test()
                .assertValuesOnly(
                        ListState.initial(),
                        ListState.loading(),
                        ListState.loaded(Collections.singletonList(new RepositoryViewModel(sampleRepository(), query)))
                );
    }

    @NonNull
    private static Repositories.Item sampleRepository() {
        final Repositories.Item item = new Repositories.Item();
        item.description = "description";
        item.fullName = "full name";
        item.language = "language";
        item.name = "name";
        final Repositories.Item.Owner owner = new Repositories.Item.Owner();
        owner.avatarUrl = "avatar url";
        owner.login = "login";
        item.owner = owner;
        return item;
    }

}