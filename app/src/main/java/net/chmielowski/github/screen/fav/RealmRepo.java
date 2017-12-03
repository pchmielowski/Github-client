package net.chmielowski.github.screen.fav;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmRepo implements RealmModel {
    public long id;

    public String name;

    static final String FAVOURITE = "FAVOURITE";
    boolean favourite;
}
