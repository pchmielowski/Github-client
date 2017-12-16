package net.chmielowski.github.screen;

import net.chmielowski.github.data.Persistence;
import net.chmielowski.github.utils.Assertions;
import net.chmielowski.github.utils.ValueIgnored;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static io.realm.Sort.DESCENDING;
import static java.util.stream.Collectors.toList;
import static net.chmielowski.github.screen.RealmSearchQuery.TIME;
import static net.chmielowski.github.utils.ValueIgnored.VALUE_IGNORED;

@Singleton
public final class PersistentQueryHistory implements QueryHistory {
    private final Persistence db;

    @Inject
    PersistentQueryHistory(final Persistence db) {
        this.db = db;
    }

    private final Subject<ValueIgnored> subject = BehaviorSubject.createDefault(VALUE_IGNORED);

    @Override
    public void searched(final String query) {
        db.executeInTransaction(realm ->
                realm.copyToRealmOrUpdate(RealmSearchQuery.from(query)));
        subject.onNext(VALUE_IGNORED);
    }

    @Override
    public Observable<Collection<String>> observe() {
        return subject
                .compose(Assertions::neverCompletes)
                .map(__ -> db.get(realm -> realm.where(RealmSearchQuery.class)
                        .findAllSorted(TIME, DESCENDING)
                        .stream()
                        .map(query -> query.text)
                        .collect(toList())));
    }
}
