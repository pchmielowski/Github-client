package net.chmielowski.github;

import android.databinding.BindingAdapter;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public final class DataBindingAdapters {
    @BindingAdapter("visibility")
    public static void setVisibility(final View view, final boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("avatarUrl")
    public static void setAvatarImageUrl(final ImageView view, final String url) {
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .fit()
                .into(view);
    }

    @BindingAdapter("visibility")
    public static void setVisibilityFab(final FloatingActionButton view, final boolean value) {
        if (value) {
            view.show();
        } else {
            view.hide();
        }
    }
}
