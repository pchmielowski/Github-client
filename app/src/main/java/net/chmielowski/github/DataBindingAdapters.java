package net.chmielowski.github;

import android.databinding.BindingAdapter;
import android.view.View;

public final class DataBindingAdapters {
    @BindingAdapter("visibility")
    public static void setVisibility(final View view, final boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }

}
