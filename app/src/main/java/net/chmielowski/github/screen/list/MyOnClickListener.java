package net.chmielowski.github.screen.list;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

class MyOnClickListener implements View.OnClickListener {

    private boolean wasAnimating = false;

    @Override
    public void onClick(View view) {
        if (wasAnimating) {
            view.clearAnimation();
            wasAnimating = true;
            return;
        }
        wasAnimating = true;

        final Animation animation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new BounceInterpolator());
        view.startAnimation(animation);
    }
}
