package net.chmielowski.github.data;

import net.chmielowski.github.Cached;

import java.util.Collection;

import lombok.EqualsAndHashCode;

// Seems toggle Android Studio can not deduce, that here we need public
@SuppressWarnings("WeakerAccess")
@EqualsAndHashCode
public final class Repositories {

    Collection<Item> items;

    public static final class Item {
        @Cached
        public String name;

        @Cached
        public String fullName;

        public Owner owner;

        public String description;

        @Cached
        public String language;

        public static final class Owner {
            @Cached
            public String login;

            @Cached
            public String avatarUrl;
        }
    }
}
