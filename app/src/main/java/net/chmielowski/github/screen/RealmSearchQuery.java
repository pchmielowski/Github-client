package net.chmielowski.github.screen;

import java.util.Date;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmSearchQuery implements RealmModel {
    @PrimaryKey
    String text;

    static final String TIME = "time";
    @SuppressWarnings("FieldCanBeLocal")
    private Long time;

    public RealmSearchQuery() {
    }

    private RealmSearchQuery(final String query, final long time) {
        this.text = query;
        this.time = time;
    }

    static RealmSearchQuery from(final String query) {
        return new RealmSearchQuery(query, new Date().getTime());
    }
}
