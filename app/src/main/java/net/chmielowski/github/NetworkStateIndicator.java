package net.chmielowski.github;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import net.chmielowski.github.databinding.ViewNetworkStateIndicatorBinding;

public final class NetworkStateIndicator extends FrameLayout {

    private final ViewNetworkStateIndicatorBinding binding;

    public NetworkStateIndicator(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        binding = ViewNetworkStateIndicatorBinding.inflate(LayoutInflater.from(getContext()), this, true);
    }

    public void hideIndicator() {
        binding.switcher.setVisibility(View.GONE);
    }

    public void onOffline() {
        setTo(binding.offline);
    }

    public void onOnline() {
        setTo(binding.online);
    }

    private void setTo(final TextView view) {
        final ViewSwitcher indicator = binding.switcher;
        indicator.setVisibility(View.VISIBLE);
        final View current = indicator.getCurrentView();
        if (current == view) {
            return;
        }
        indicator.showNext();
    }
}
