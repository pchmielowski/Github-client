package net.chmielowski.github;

import android.databinding.BindingAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

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

    @BindingAdapter("visibility")
    public static void setVisibilityRecycler(final RecyclerView view, final boolean value) {
        if (value) {
            view.setVisibility(View.VISIBLE);
        } else {
            final AnimationSet animationSet = new AnimationSet(true);
            final TranslateAnimation translate = new TranslateAnimation(0, 0, 0, -1000);
            translate.setDuration(1000);
            animationSet.addAnimation(translate);
            final ScaleAnimation scale = new ScaleAnimation(1, 1, 1, 0);
            scale.setDuration(250);
            animationSet.addAnimation(scale);
            animationSet.setInterpolator(new AccelerateInterpolator());
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animationSet);
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
