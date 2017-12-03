package net.chmielowski.github.data;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmRepo implements RealmModel {
    static final String ID = "id";
    @PrimaryKey
    public long id;

    public String name;

    public String owner;

    public static final String FAVOURITE = "favourite";
    boolean favourite;
}
