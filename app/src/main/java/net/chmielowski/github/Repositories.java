package net.chmielowski.github;

import java.util.Collection;

final class Repositories {

    Collection<Item> items;

    final class Item {
        Long id;
        String fullName;
    }
}
