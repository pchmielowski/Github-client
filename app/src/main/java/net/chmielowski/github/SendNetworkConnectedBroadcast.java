package net.chmielowski.github;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

public class SendNetworkConnectedBroadcast extends GcmTaskService {
    public static final String NETWORK_AVAILABLE = "NETWORK_AVAILABLE";
    private final Context instance;

    public SendNetworkConnectedBroadcast() {
        // TODO: inject
        instance = CustomApplication.INSTANCE;
    }

    @Override
    public int onRunTask(final TaskParams params) {
        LocalBroadcastManager.getInstance(instance)
                .sendBroadcast(new Intent(NETWORK_AVAILABLE));
        return 0;
    }

}

