package net.chmielowski.github.pagination;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import net.chmielowski.github.utils.ValueIgnored;

import io.reactivex.Observable;

public final class RxPagination {
    private RxPagination() {}

    public static Observable<ValueIgnored> scrolledCloseToEnd(
            final RecyclerView view, final LinearLayoutManager manager) {
        return RxRecyclerView.scrollEvents(view)
                .filter(RxPagination::scrolledDown)
                .filter(event -> closeToEnd(manager))
                .map(__ -> ValueIgnored.VALUE_IGNORED);
    }

    private static boolean closeToEnd(final LinearLayoutManager manager) {
        return (manager.findLastVisibleItemPosition() + 2) > manager.getItemCount();
    }

    private static boolean scrolledDown(final RecyclerViewScrollEvent event) {
        return event.dy() > 0;
    }
}
