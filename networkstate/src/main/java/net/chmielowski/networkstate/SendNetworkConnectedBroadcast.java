package net.chmielowski.networkstate;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

public final class SendNetworkConnectedBroadcast extends GcmTaskService {
    public static final String NETWORK_AVAILABLE = "NETWORK_AVAILABLE";

    @Override
    public int onRunTask(final TaskParams params) {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(NETWORK_AVAILABLE));
        return 0;
    }

}

