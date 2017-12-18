package net.chmielowski.github.data;


import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

// TODO: authentication package
@RealmClass
public class RealmUser implements RealmModel {

    @PrimaryKey
    int id;

    String token;

    RealmUser(final String token) {
        this.id = 0; // one instance
        this.token = token;
    }

    public RealmUser() {
    }
}
