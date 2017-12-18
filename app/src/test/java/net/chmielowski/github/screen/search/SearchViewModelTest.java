package net.chmielowski.github.screen.search;

import android.support.annotation.NonNull;

import net.chmielowski.github.data.Repositories;
import net.chmielowski.github.data.RepositoryDataSource;
import net.chmielowski.github.network.NetworkState;
import net.chmielowski.github.screen.ListState;
import net.chmielowski.github.screen.QueryHistory;
import net.chmielowski.github.screen.RepositoryViewModel;
import net.chmielowski.github.screen.search.SearchViewModel.Query;
import net.chmielowski.github.utils.ValueIgnored;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.function.Consumer;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.observers.BaseTestConsumer;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static io.reactivex.Maybe.just;
import static io.reactivex.Observable.never;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static net.chmielowski.github.screen.ListState.empty;
import static net.chmielowski.github.screen.ListState.loaded;
import static net.chmielowski.github.screen.ListState.loading;
import static net.chmielowski.github.screen.search.SearchViewModel.ErrorMessage.EMPTY_QUERY;
import static net.chmielowski.github.screen.search.SearchViewModel.Query.firstPage;
import static net.chmielowski.github.utils.TestUtils.sampleRepository;
import static net.chmielowski.github.utils.ValueIgnored.VALUE_IGNORED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public final class SearchViewModelTest {

    private QueryHistory history;
    private RepositoryDataSource service;
    private static final String QUERY_TEXT = "some query";

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(RepositoryDataSource.class);
        history = Mockito.mock(QueryHistory.class);
    }

    @Test
    public void oneQueryInOnline() throws Exception {
        oneQuery(alwaysOnline(),
                observer -> observer.assertValuesOnly(
                        loading(),
                        loaded(singletonList(new RepositoryViewModel(sampleRepository(), QUERY_TEXT)))
                )
        );
    }

    @Test
    public void oneQueryInOffline() throws Exception {
        oneQuery(alwaysOffline(),
                BaseTestConsumer::assertEmpty
        );
    }

    private void oneQuery(final NetworkState networkState,
                          final Consumer<TestObserver<ListState>> assertion) {
        setupServiceToReturnSingletonListOnFirstQuery();

        final TestObserver<ListState> test = new SearchViewModel(service, history, networkState)
                .replaceResults(query(QUERY_TEXT))
                .test();

        assertion.accept(test);
    }

    @Test
    public void hidesLoadingOnFailure() throws Exception {
        when(service.repositories(firstPage(QUERY_TEXT)))
                .thenReturn(Maybe.empty());

        createViewModel()
                .replaceResults(query(QUERY_TEXT))
                .test()
                .assertValuesOnly(
                        loading(),
                        empty()
                );
    }

    @Test
    public void showsErrorOnEmptyQuery() throws Exception {
        emptyQuery("");
    }

    @Test
    public void showsErrorOnQueryWithSpacesOnly() throws Exception {
        emptyQuery("         ");
    }

    private void emptyQuery(final String query) {
        final SearchViewModel model = createViewModel();

        final TestObserver<SearchViewModel.ErrorMessage> errorTest = model.error().test();
        model.replaceResults(query(query))
                .test()
                .assertEmpty();

        assertThat(model.searchMode.get(), is(true));
        errorTest.assertValuesOnly(EMPTY_QUERY);
    }

    @Test
    public void clearsQueryAfterSearch() {
        setupServiceToReturnSingletonListOnFirstQuery();

        final SearchViewModel model = createViewModel();

        model.query.set(QUERY_TEXT);
        model.replaceResults(Observable.just(VALUE_IGNORED), never())
                .subscribe();

        assertThat(model.query.get(), is(""));
    }

    @Test
    public void scrolledToTheEndOnceInOnline() throws Exception {
        final List<Repositories.Item> secondPage = singletonList(sampleRepository());

        when(service.repositories(firstPage(QUERY_TEXT)))
                .thenReturn(just(emptyList()));
        when(service.repositories(new Query(1, QUERY_TEXT)))
                .thenReturn(just(secondPage));

        final SearchViewModel model = createViewModel();
        performFirstSearch(model);

        final TestObserver<ListState> test = model
                .appendResults(emitOnce())
                .test();

        test.assertValuesOnly(
                loading(),
                loaded(mapToViewModel(secondPage, QUERY_TEXT))
        );
    }

    @Test
    public void scrolledToTheEndTwice() throws Exception {
        final List<Repositories.Item> firstPage = asList(sampleRepository(), sampleRepository());
        final List<Repositories.Item> secondPage = asList(sampleRepository(), sampleRepository(),
                sampleRepository());
        final List<Repositories.Item> thirdPage = singletonList(sampleRepository());

        when(service.repositories(firstPage(QUERY_TEXT)))
                .thenReturn(just(firstPage));
        when(service.repositories(new Query(1, QUERY_TEXT)))
                .thenReturn(just(secondPage));
        when(service.repositories(new Query(2, QUERY_TEXT)))
                .thenReturn(just(thirdPage));

        final Subject<ValueIgnored> scrolledSubject = PublishSubject.create();

        final SearchViewModel model = createViewModel();
        final TestObserver<ListState> test = model
                .appendResults(scrolledSubject)
                .test();

        performFirstSearch(model);
        scrolledSubject.onNext(VALUE_IGNORED);
        scrolledSubject.onNext(VALUE_IGNORED);

        test.assertValuesOnly(
                loading(),
                loaded(mapToViewModel(secondPage, QUERY_TEXT)),
                loading(),
                loaded(mapToViewModel(thirdPage, QUERY_TEXT))
        );
    }

    @Test
    public void scrolledToTheEndTwiceBeforeLoadingFinished() throws Exception {
        final List<Repositories.Item> firstPage = asList(sampleRepository(), sampleRepository());

        when(service.repositories(firstPage(QUERY_TEXT)))
                .thenReturn(just(firstPage));
        when(service.repositories(new Query(1, QUERY_TEXT)))
                .thenReturn(Maybe.never());

        final Subject<ValueIgnored> scrolledSubject = PublishSubject.create();

        final SearchViewModel model = createViewModel();
        final TestObserver<ListState> test = model
                .appendResults(scrolledSubject)
                .test();

        performFirstSearch(model);
        scrolledSubject.onNext(VALUE_IGNORED);
        scrolledSubject.onNext(VALUE_IGNORED);

        test.assertValuesOnly(loading());
    }

    @Test
    public void scrolledToTheEndOnceAndSendNextQuery() throws Exception {
        final String query = "first";
        final String nextQuery = "second";

        final List<Repositories.Item> firstPage = asList(sampleRepository(), sampleRepository());
        final List<Repositories.Item> secondPage = singletonList(sampleRepository());
        final List<Repositories.Item> firstPageNextQuery = asList(sampleRepository(), sampleRepository(), sampleRepository());

        when(service.repositories(SearchViewModel.Query.firstPage(query)))
                .thenReturn(just(firstPage));
        when(service.repositories(new SearchViewModel.Query(1, query)))
                .thenReturn(just(secondPage));
        when(service.repositories(SearchViewModel.Query.firstPage(nextQuery)))
                .thenReturn(just(firstPageNextQuery));

        final Subject<ValueIgnored> scrolledSubject = PublishSubject.create();
        final Subject<CharSequence> querySubject = PublishSubject.create();

        final SearchViewModel model = createViewModel();

        final TestObserver<ListState> replaceObserver = model
                .replaceResults(querySubject)
                .test();

        final TestObserver<ListState> appendObserver = model
                .appendResults(scrolledSubject)
                .test();

        querySubject.onNext(query);
        scrolledSubject.onNext(VALUE_IGNORED);
        model.enterSearchMode();
        querySubject.onNext(nextQuery);

        replaceObserver.assertValuesOnly(
                loading(),
                loaded(mapToViewModel(firstPage, query)),
                loading(),
                loaded(mapToViewModel(firstPageNextQuery, nextQuery))
        );

        appendObserver.assertValuesOnly(
                loading(),
                loaded(mapToViewModel(secondPage, query))
        );
    }

    @Test
    public void backPressedBeforeAnyAction() throws Exception {
        final SearchViewModel model = createViewModel();

        final boolean modeBefore = model.searchMode.get();

        model.onBackPressed()
                .test()
                .assertValue(false);

        assertThat(model.searchMode.get(), is(modeBefore));
    }

    @Test
    public void backPressedAfterSearch() throws Exception {
        final SearchViewModel model = createViewModel();

        setupServiceToReturnSingletonListOnFirstQuery();
        performFirstSearch(model);

        final boolean modeBefore = model.searchMode.get();
        model.onBackPressed()
                .test()
                .assertResult(false);

        assertThat(model.searchMode.get(), is(modeBefore));
    }

    @Test
    public void backPressedAfterSearchAndEnterSearchMode() throws Exception {
        final SearchViewModel model = createViewModel();

        setupServiceToReturnSingletonListOnFirstQuery();
        performFirstSearch(model);

        model.enterSearchMode();

        final boolean modeBefore = model.searchMode.get();
        model.onBackPressed()
                .test()
                .assertResult(true);

        assertThat(model.searchMode.get(), is(!modeBefore));
    }

    private void setupServiceToReturnSingletonListOnFirstQuery() {
        when(service.repositories(firstPage(QUERY_TEXT)))
                .thenReturn(just(singletonList(sampleRepository())));
    }

    @NonNull
    private SearchViewModel createViewModel() {
        return new SearchViewModel(service, history, alwaysOnline());
    }

    private static void performFirstSearch(final SearchViewModel model) {
        model.replaceResults(query(QUERY_TEXT)).subscribe();
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
                .thenReturn(never());
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
//    TODO: uncomment
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
//        when(service.repositories(SearchViewModel.Query.firstPage(query)))
//                .thenReturn(just(firstPage));
//        when(service.repositories(new SearchViewModel.Query(1, query)))
//                .thenReturn(just(secondPage));
//        when(service.repositories(SearchViewModel.Query.firstPage(nextQuery)))
//                .thenReturn(just(firstPageNextQuery));
//        when(service.repositories(new SearchViewModel.Query(1, nextQuery)))
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