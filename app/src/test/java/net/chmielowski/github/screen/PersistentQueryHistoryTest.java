package net.chmielowski.github.screen;

import net.chmielowski.github.data.Persistence;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.function.Function;

import io.reactivex.observers.TestObserver;

import static java.util.Arrays.asList;

public class PersistentQueryHistoryTest {
    @SuppressWarnings("unchecked")
    @Test
    public void emitsDataCorrectly() throws Exception {
        final Persistence db = Mockito.mock(Persistence.class);
        final PersistentQueryHistory history = new PersistentQueryHistory(db);

        Mockito.when(db.get(Mockito.any(Function.class)))
                .thenReturn(asList("a", "b"))
                .thenReturn(asList("a", "b", "c"));

        final TestObserver<Collection<String>> testObserver = history.observe().test();

        testObserver.assertValues(asList("a", "b"));
        history.searched("c");

        testObserver.assertValues(asList("a", "b"), asList("a", "b", "c"));
    }
}