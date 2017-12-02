package net.chmielowski.github.screen.list;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmString implements RealmModel {
    @PrimaryKey
    int id;

    String value;

    static RealmString from(String val) {
        final RealmString object = new RealmString();
        object.id = 0; // Single instance
        return object;
    }
}
