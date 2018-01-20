package net.chmielowski.networkstate;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import net.chmielowski.github.ApplicationContext;

import javax.inject.Inject;

import static net.chmielowski.github.CustomApplication.get;

public final class SendNetworkConnectedBroadcast extends GcmTaskService {
    public static final String NETWORK_AVAILABLE = "NETWORK_AVAILABLE";

    @Inject
    @ApplicationContext
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        get(this).mainComponent().inject(this);
    }

    @Override
    public int onRunTask(final TaskParams params) {
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(new Intent(NETWORK_AVAILABLE));
        return 0;
    }

}

