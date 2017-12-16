package net.chmielowski.github.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.Task;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static java.util.Objects.requireNonNull;

@Singleton
public final class BasicNetworkState implements NetworkState {
    private final ConnectivityManager connectivityManager;
    private final GcmNetworkManager networkManager;
    private final LocalBroadcastManager broadcastManager;

    @Inject
    BasicNetworkState(final ConnectivityManager connectivityManager,
                      final GcmNetworkManager networkManager,
                      final LocalBroadcastManager broadcastManager) {
        this.connectivityManager = requireNonNull(connectivityManager);
        this.networkManager = requireNonNull(networkManager);
        this.broadcastManager = broadcastManager;

    }

    @Override
    @NonNull
    public <T> Observable<T> requireOnline(@NonNull final Observable<T> observable) {
        final boolean isOnline = isOnline();
        if (!isOnline) {
            subject.onNext(State.OFFLINE);
            waitForOnline();
        }
        return isOnline ? observable : Observable.empty();
    }

    @Override
    public boolean isOnline() {
        final boolean isOnline = Optional.ofNullable(connectivityManager.getActiveNetworkInfo())
                .map(NetworkInfo::isConnected)
                .orElse(false);
        if (!isOnline) {
            subject.onNext(State.OFFLINE);
            waitForOnline();
        }
        return isOnline;
    }


    private final static String TAG = "NETWORK_CONNECTED";


    private final BroadcastReceiver networkConnectedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            subject.onNext(State.ONLINE);
            broadcastManager.unregisterReceiver(networkConnectedBroadcastReceiver);
        }
    };

    private void waitForOnline() {
        final long ONE_HOUR = 3600L;
        final OneoffTask task = new OneoffTask.Builder()
                .setService(SendNetworkConnectedBroadcast.class)
                .setTag(TAG)
                .setExecutionWindow(0L, ONE_HOUR)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .build();
        networkManager.schedule(task);
        broadcastManager.registerReceiver(
                networkConnectedBroadcastReceiver,
                new IntentFilter(SendNetworkConnectedBroadcast.NETWORK_AVAILABLE));

    }


    public enum State {
        ONLINE, OFFLINE
    }

    private final Subject<State> subject = PublishSubject.create();

    @Override
    public Observable<State> observe() {
        return subject;
    }
}
