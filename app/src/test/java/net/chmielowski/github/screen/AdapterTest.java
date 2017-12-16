package net.chmielowski.github.screen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AdapterTest {
    @Test
    public void loading() throws Exception {
        final Adapter adapter = new Adapter(RuntimeEnvironment.application.getApplicationContext());

        adapter.append(ListState.loading());

        assertThat(adapter.getItemCount(), is(1));
    }
}