package net.chmielowski.github.screen;

import net.chmielowski.github.data.Persistence;
import net.chmielowski.github.pagination.ValueIgnored;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static io.realm.Sort.DESCENDING;
import static java.util.stream.Collectors.toList;
import static net.chmielowski.github.pagination.ValueIgnored.VALUE_IGNORED;
import static net.chmielowski.github.screen.RealmSearchQuery.TIME;

@Singleton
public final class PersistentQueryHistory implements QueryHistory {
    private final Persistence realm;

    @Inject
    PersistentQueryHistory(final Persistence realm) {
        this.realm = realm;
    }

    private final Subject<ValueIgnored> subject = BehaviorSubject.createDefault(VALUE_IGNORED);

    @Override
    public void searched(final String query) {
        realm.executeInTransaction(realm ->
                realm.copyToRealmOrUpdate(RealmSearchQuery.from(query)));
        subject.onNext(VALUE_IGNORED);
    }

    @Override
    public Observable<Collection<String>> observe() {
        return subject.map(__ ->
                realm.get(realm -> realm.where(RealmSearchQuery.class)
                        .findAllSorted(TIME, DESCENDING)
                        .stream()
                        .map(query -> query.text)
                        .collect(toList())));
    }
}
