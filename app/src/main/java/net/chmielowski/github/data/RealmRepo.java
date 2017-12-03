package net.chmielowski.github.data;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmRepo implements RealmModel {
    static final String ID = "id";
    @PrimaryKey
    public String id;

    public String owner;

    public String name;

    public String avatar;

    public RealmRepo() {
    }

    RealmRepo(final Repositories.Item json) {
        this.id = json.fullName;
        this.name = json.name;
        this.owner = json.owner.login;
        this.avatar = json.owner.avatarUrl;
    }
}
