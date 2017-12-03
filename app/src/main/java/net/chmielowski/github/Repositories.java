package net.chmielowski.github;

import java.util.Collection;

// Seems like Android Studio can not deduce, that here we need public
@SuppressWarnings("WeakerAccess")
public final class Repositories {

    Collection<Item> items;

    public static final class Item {
        public Long id;
        public String fullName;
    }
}
