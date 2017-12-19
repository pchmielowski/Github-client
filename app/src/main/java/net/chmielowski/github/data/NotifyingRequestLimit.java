package net.chmielowski.github.data;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import net.chmielowski.github.ApplicationContext;
import net.chmielowski.github.R;

import javax.inject.Inject;

final class NotifyingRequestLimit implements RequestLimit {
    private final Context context;
    private final User user;

    @Inject
    NotifyingRequestLimit(@ApplicationContext Context context, final User user) {
        this.context = context;
        this.user = user;
    }

    @Override
    public void onReached() {
        new Handler(Looper.getMainLooper())
                .post(() -> new AlertDialog.Builder(context)
                        .setTitle(R.string.msg_request_limit_reached)
                        .setMessage(message())
                        .setPositiveButton(android.R.string.ok, null)
                        .show());
    }

    private int message() {
        return user.token()
                .map(__ -> R.string.msg_log_in)
                .orElse(R.string.msg_wait);
    }
}
