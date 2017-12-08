package net.chmielowski.github.screen;

import net.chmielowski.github.pagination.ValueIgnored;

import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public final class PersistentQueryHistory implements QueryHistory {
    @Inject
    PersistentQueryHistory() {
    }

    private final Map<String, Calendar> map = new HashMap<>();

    private final Subject<ValueIgnored> subject = PublishSubject.create();

    @Override
    public void searched(final String query) {
        map.put(query, Calendar.getInstance());
        subject.onNext(ValueIgnored.VALUE_IGNORED);
    }

    @Override
    public Observable<Collection<String>> observe() {
        return subject.map(__ -> map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
    }
}
