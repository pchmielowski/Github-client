package net.chmielowski.github.data;

import net.chmielowski.github.utils.ValueIgnored;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

@Singleton
final class NotifyingRequestLimit implements RequestLimit {

    @Inject
    NotifyingRequestLimit() {
    }

    @Override
    public void onReached() {
        subject.onNext(ValueIgnored.VALUE_IGNORED);
    }

    private final Subject<ValueIgnored> subject = PublishSubject.create();

    @Override
    public Observable<ValueIgnored> observe() {
        return subject;
    }

}
