package net.chmielowski.github.data;


import java.util.Collection;
import java.util.Date;

import lombok.EqualsAndHashCode;

// Seems toggle Android Studio can not deduce, that here we need public
@SuppressWarnings("WeakerAccess")
@EqualsAndHashCode
public final class Repositories {

    Collection<Item> items;

    public static final class Item {
        public String name;

        public String fullName;

        public Owner owner;

        public String description;

        public String language;

        public Date createdAt;

        public Date updatedAt;

        public String homepage;

        public int forks;

        public int openIssues;

        public static final class Owner {
            public String login;

            public String avatarUrl;
        }
    }
}
