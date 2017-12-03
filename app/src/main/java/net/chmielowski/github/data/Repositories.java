package net.chmielowski.github.data;

import java.util.Collection;

// Seems like Android Studio can not deduce, that here we need public
@SuppressWarnings("WeakerAccess")
public final class Repositories {

    Collection<Item> items;

    public static final class Item {
        public String name;

        public String fullName;

        public Owner owner;

        public static final class Owner {
            public String login;

            public String avatarUrl;
        }
    }
}
