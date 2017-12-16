package net.chmielowski.github.screen;

import android.support.annotation.NonNull;

import net.chmielowski.github.data.RepoService;
import net.chmielowski.github.data.Repositories;
import net.chmielowski.github.network.NetworkState;
import net.chmielowski.github.utils.ValueIgnored;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static io.reactivex.Maybe.just;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static net.chmielowski.github.screen.ListState.loaded;
import static net.chmielowski.github.screen.ListState.loading;
import static net.chmielowski.github.screen.SearchViewModel.Query.firstPage;
import static net.chmielowski.github.utils.TestUtils.sampleRepository;
import static net.chmielowski.github.utils.ValueIgnored.VALUE_IGNORED;
import static org.mockito.Mockito.when;

// TODO: remove redundant tests
public final class SearchViewModelTest {

    private QueryHistory history;
    private RepoService service;

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(RepoService.class);
        history = Mockito.mock(QueryHistory.class);
    }

    @Test
    public void oneQueryInOnline() throws Exception {
        final String query = "some query";

        when(service.items(firstPage(query)))
                .thenReturn(just(singletonList(sampleRepository())));

        new SearchViewModel(service, history, alwaysOnline())
                .replaceResults(query(query))
                .test()
                .assertValuesOnly(
                        loading(),
                        loaded(singletonList(new RepositoryViewModel(sampleRepository(), query)))
                );
    }

    @Test
    public void oneQueryInOffline() throws Exception {
        final String query = "some query";

        when(service.items(firstPage(query)))
                .thenReturn(just(singletonList(sampleRepository())));

        new SearchViewModel(service, history, alwaysOffline())
                .replaceResults(query(query))
                .test()
                .assertEmpty();
    }

    @Test
    public void scrolledToTheEndOnceInOnline() throws Exception {
        final String query = "third";

        final List<Repositories.Item> secondPage = singletonList(sampleRepository());

        when(service.items(firstPage(query)))
                .thenReturn(just(emptyList()));
        when(service.items(new SearchViewModel.Query(1, query)))
                .thenReturn(just(secondPage));

        final SearchViewModel model = new SearchViewModel(service, history, alwaysOnline());
        model.replaceResults(query(query)).subscribe();

        final TestObserver<ListState> test = model
                .appendResults(emitOnce())
                .test();

        test.assertValuesOnly(
                loading(),
                loaded(mapToViewModel(secondPage, query))
        );
    }

    @SuppressWarnings("unchecked")
    private NetworkState alwaysOnline() {
        final NetworkState mock = Mockito.mock(NetworkState.class);
        when(mock.requireOnline(Mockito.any(Observable.class)))
                .thenAnswer(withFirstArgument());
        when(mock.isOnline()).thenReturn(true);
        return mock;
    }

    @SuppressWarnings("unchecked")
    private NetworkState alwaysOffline() {
        final NetworkState mock = Mockito.mock(NetworkState.class);
        when(mock.requireOnline(Mockito.any(Observable.class)))
                .thenReturn(Observable.never());
        when(mock.isOnline()).thenReturn(false);
        return mock;
    }

    @NonNull
    private static Answer withFirstArgument() {
        return invocation -> invocation.getArguments()[0];
    }

    private static Observable<ValueIgnored> emitOnce() {
        return Observable.create(e -> e.onNext(VALUE_IGNORED));
    }
//
//    @Test
//    public void scrolledToTheEndTwice() throws Exception {
//        final String query = "fourth";
//
//        final List<Repositories.Item> firstPage = asList(sampleRepository(), sampleRepository());
//        final List<Repositories.Item> secondPage = asList(sampleRepository(), sampleRepository(),
//                sampleRepository());
//        final List<Repositories.Item> thirdPage = singletonList(sampleRepository());
//
//        when(service.items(SearchViewModel.Query.firstPage(query)))
//                .thenReturn(just(firstPage));
//        when(service.items(new SearchViewModel.Query(1, query)))
//                .thenReturn(just(secondPage));
//        when(service.items(new SearchViewModel.Query(2, query)))
//                .thenReturn(just(thirdPage));
//
//        final Subject<ValueIgnored> scrolledSubject = PublishSubject.create();
//
//        final TestObserver<ListState> test = new SearchViewModel(service, new QueryHistory())
//                .appendResults(query(query), scrolledSubject)
//                .test();
//        scrolledSubject.onNext(VALUE_IGNORED);
//        scrolledSubject.onNext(VALUE_IGNORED);
//
//        test.assertValuesOnly(
//                initial(),
//                loading(),
//                loaded(mapToViewModel(firstPage, query)),
//                loading(),
//                loaded(mapToViewModel(secondPage, query)),
//                loading(),
//                loaded(mapToViewModel(thirdPage, query))
//        );
//    }
//
//    @Test
//    public void scrolledToTheEndOnceAndSendNextQuery() throws Exception {
//        final String query = "first";
//        final String nextQuery = "second";
//
//        final List<Repositories.Item> firstPage = asList(sampleRepository(), sampleRepository());
//        final List<Repositories.Item> secondPage = singletonList(sampleRepository());
//        final List<Repositories.Item> firstPageNextQuery = asList(sampleRepository(), sampleRepository(), sampleRepository());
//
//        when(service.items(SearchViewModel.Query.firstPage(query)))
//                .thenReturn(just(firstPage));
//        when(service.items(new SearchViewModel.Query(1, query)))
//                .thenReturn(just(secondPage));
//        when(service.items(SearchViewModel.Query.firstPage(nextQuery)))
//                .thenReturn(just(firstPageNextQuery));
//
//        final Subject<ValueIgnored> scrolledSubject = PublishSubject.create();
//        final Subject<String> querySubject = PublishSubject.create();
//
//        final TestObserver<ListState> test = new SearchViewModel(service, new QueryHistory())
//                .appendResults(scrolledSubject)
//                .test();
//
//        querySubject.onNext(query);
//        scrolledSubject.onNext(VALUE_IGNORED);
//        querySubject.onNext(nextQuery);
//
//        test.assertValuesOnly(
//                initial(),
//                loading(),
//                loaded(mapToViewModel(firstPage, query)),
//                loading(),
//                loaded(mapToViewModel(secondPage, query)),
//                loading(),
//                loaded(mapToViewModel(firstPageNextQuery, nextQuery))
//        );
//    }
//
//    @Test
//    public void scrolledToTheEndOnceAndSendNextQueryAndScrolledToEnd() throws Exception {
//        final String query = "first";
//        final String nextQuery = "second";
//
//        final List<Repositories.Item> firstPage = asList(sampleRepository(), sampleRepository());
//        final List<Repositories.Item> secondPage = singletonList(sampleRepository());
//        final List<Repositories.Item> firstPageNextQuery = asList(sampleRepository(), sampleRepository(), sampleRepository());
//        final List<Repositories.Item> secondPageNextQuery = asList(sampleRepository(), sampleRepository(), sampleRepository(), sampleRepository());
//
//        when(service.items(SearchViewModel.Query.firstPage(query)))
//                .thenReturn(just(firstPage));
//        when(service.items(new SearchViewModel.Query(1, query)))
//                .thenReturn(just(secondPage));
//        when(service.items(SearchViewModel.Query.firstPage(nextQuery)))
//                .thenReturn(just(firstPageNextQuery));
//        when(service.items(new SearchViewModel.Query(1, nextQuery)))
//                .thenReturn(just(secondPageNextQuery));
//
//        final Subject<ValueIgnored> scrolledSubject = PublishSubject.create();
//        final Subject<String> querySubject = PublishSubject.create();
//
//        final TestObserver<ListState> test = new SearchViewModel(service, new QueryHistory())
//                .appendResults(scrolledSubject)
//                .test();
//
//        querySubject.onNext(query);
//        scrolledSubject.onNext(VALUE_IGNORED);
//        querySubject.onNext(nextQuery);
//        scrolledSubject.onNext(VALUE_IGNORED);
//
//        test.assertValuesOnly(
//                initial(),
//                loading(),
//                loaded(mapToViewModel(firstPage, query)),
//                loading(),
//                loaded(mapToViewModel(secondPage, query)),
//                loading(),
//                loaded(mapToViewModel(firstPageNextQuery, nextQuery)),
//                loading(),
//                loaded(mapToViewModel(secondPageNextQuery, nextQuery))
//        );
//    }


    private static List<RepositoryViewModel> mapToViewModel(final List<Repositories.Item> results, final String query) {
        return results.stream().map(it -> new RepositoryViewModel(it, query)).collect(toList());
    }

    private static Observable<CharSequence> query(final String query) {
        return Observable.create(e -> e.onNext(query));
    }

}