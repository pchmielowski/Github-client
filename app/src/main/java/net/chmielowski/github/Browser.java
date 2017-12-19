package net.chmielowski.github;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import javax.inject.Inject;

public final class Browser {
    private final Context context;

    @Inject
    Browser(@ActivityContext final Context context) {
        this.context = context;
    }

    public void open(String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
