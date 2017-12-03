package net.chmielowski.github.screen.list;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmString implements RealmModel {
    @PrimaryKey
    private int id;

    public String value;

    public static RealmString from(final String val) {
        final RealmString object = new RealmString();
        object.id = 0; // Single instance
        object.value = val;
        return object;
    }
}
