package net.chmielowski.github.data;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmRepo implements RealmModel {
    static final String NAME = "name";
    @PrimaryKey
    public String name;

    public RealmRepo() {
    }

    RealmRepo(final String name) {
        this.name = name;
    }
}
