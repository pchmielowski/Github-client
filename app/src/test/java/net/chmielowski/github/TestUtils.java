package net.chmielowski.github;

import android.support.annotation.NonNull;

import net.chmielowski.github.data.Repositories;

import org.mockito.stubbing.Answer;

import java.util.Date;

public final class TestUtils {
    private TestUtils() {
        throw new AssertionError("No instances!");
    }

    @NonNull
    public static Repositories.Item sampleRepository() {
        final Repositories.Item item = new Repositories.Item();
        item.description = "description";
        item.language = "language";
        item.name = "name";
        final Repositories.Item.Owner owner = new Repositories.Item.Owner();
        owner.avatarUrl = "avatar url";
        owner.login = "name";
        item.owner = owner;
        item.fullName = owner.login + "/" + item.name;
        item.createdAt = new Date();
        item.updatedAt = new Date();
        item.openIssues = 234;
        item.forks = 34;
        return item;
    }

    @NonNull
    public static Answer withFirstArgument() {
        return invocation -> invocation.getArguments()[0];
    }
}
