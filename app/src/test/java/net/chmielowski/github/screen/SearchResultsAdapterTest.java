package net.chmielowski.github.screen;

import android.support.annotation.NonNull;

import net.chmielowski.github.TestApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.function.Consumer;

import static java.util.Collections.emptyList;
import static net.chmielowski.github.screen.ListState.loaded;
import static net.chmielowski.github.screen.ListState.loading;
import static net.chmielowski.github.screen.SearchResultsAdapter.TYPE_SPINNER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

@SuppressWarnings("unchecked")
@Config(application = TestApplication.class)
@RunWith(RobolectricTestRunner.class)
public class SearchResultsAdapterTest {
    @Test
    public void appendLoading() throws Exception {
        testLoading(adapter -> adapter.append(loading()));
    }

    @Test
    public void replaceLoading() throws Exception {
        testLoading(adapter -> adapter.replace(loading()));
    }

    private void testLoading(final Consumer<SearchResultsAdapter> action) {
        final SearchResultsAdapter adapter = createAdapter();

        action.accept(adapter);

        assertThat(adapter.getItemCount(), is(1));
        assertThat(adapter.getItemViewType(0), is(TYPE_SPINNER));
    }

    @Test
    public void loadedEmptyList() throws Exception {
        final SearchResultsAdapter adapter = createAdapter();

        adapter.append(loading());
        adapter.replace(loaded(emptyList()));

        assertThat(adapter.getItemCount(), is(0));
    }

    @NonNull
    private static SearchResultsAdapter createAdapter() {
        return new SearchResultsAdapter(application.getBaseContext());
    }
}