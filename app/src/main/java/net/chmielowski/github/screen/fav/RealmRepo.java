package net.chmielowski.github.screen.fav;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmRepo implements RealmModel {
    public static final String ID = "id";
    @PrimaryKey
    public long id;

    public String name;

    static final String FAVOURITE = "favourite";
    public boolean favourite;
}
