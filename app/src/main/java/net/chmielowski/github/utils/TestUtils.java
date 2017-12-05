package net.chmielowski.github.utils;

import android.support.annotation.NonNull;

import net.chmielowski.github.data.Repositories;

public class TestUtils {
    @NonNull
    public static Repositories.Item sampleRepository() {
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
