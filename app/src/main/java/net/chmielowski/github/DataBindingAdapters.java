package net.chmielowski.github;

import android.databinding.BindingAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;

public final class DataBindingAdapters {
    @BindingAdapter("visibility")
    public static void setVisibility(final View view, final boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("visibility")
    public static void setVisibilityFab(final FloatingActionButton view, final boolean value) {
        if (value) {
            view.show();
        } else {
            view.hide();
        }
    }

    @BindingAdapter("rotating")
    public static void setFabRotating(final FloatingActionButton view, final boolean value) {
        if (value) {
            final Animation animation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(2000);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setInterpolator(new BounceInterpolator());
            view.startAnimation(animation);
        } else {
            view.clearAnimation();
        }
    }

}
