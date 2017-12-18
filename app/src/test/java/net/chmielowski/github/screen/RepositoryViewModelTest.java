package net.chmielowski.github.screen;

import junit.framework.Assert;

import net.chmielowski.github.TestUtils;
import net.chmielowski.github.data.Repositories;

import org.junit.Test;

public class RepositoryViewModelTest {
    @Test
    public void returnsCorrectlyFormattedName() throws Exception {
        test("", "", 0, 0);
        test("abcd", "", 0, 0);
        test("abcd", "abcd", 0, 4);
        test("abcd", "ab", 0, 2);
        test("abcd", " ab ", 0, 2);
        test("abcd", "bc", 1, 3);
        test("abcd", "cd", 2, 4);
        test("abCd", "cD", 2, 4);
        test("abcd", "cd ", 2, 4);
        test("abcd", "efgh", -1, -1);
    }

    private void test(final String name, final String query, final int start, final int end) {
        final Repositories.Item repo = TestUtils.sampleRepository();
        repo.name = name;

        Assert.assertEquals(
                new RepositoryViewModel.FormattedText(name, start, end),
                new RepositoryViewModel(repo, query).name());
    }

}