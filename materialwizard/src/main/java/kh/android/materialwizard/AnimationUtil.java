package kh.android.materialwizard;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

/**
 * Created by liangyuteng0927 on 17-1-17.
 * Email: liangyuteng12345@gmail.com
 */

final class AnimationUtil {
    static void changeText (final String newText, final TextView textView) {
        fadeOut(textView,200,new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation p1) {
                // Should do something, but this method is useless yet…
            }

            @Override
            public void onAnimationEnd(Animation p1) {
                textView.setText(newText);
                fadeIn(textView,200,null);
            }

            @Override
            public void onAnimationRepeat(Animation p1) {
                // Google thinks that this method should also do something!
                // Why there are so many useless methods in the API?
            }
        });
    }
    /**
     * @author Rachel
     * @param v View you want to fade out
     * @param duration Time anim lasts
     * @param listener What do you want to do in the end or when anim starts?
     */
    static void fadeOut(View v,long duration,Animation.AnimationListener listener){
        // Basic Animation definition
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1,0);
        fadeOutAnimation.setDuration(duration);
        if (listener != null)
            fadeOutAnimation.setAnimationListener(listener);
        v.startAnimation(fadeOutAnimation);
    }

    /**
     * @author Rachel
     * @param v View you want to fade in
     * @param duration Time anim lasts
     * @param listener What do you want to do in the end or when anim starts?
     */
    static void fadeIn(View v,long duration,Animation.AnimationListener listener){
        // Again, just basic Animation definition
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0,1);
        fadeInAnimation.setDuration(duration);
        if (listener != null)
            fadeInAnimation.setAnimationListener(listener);
        v.startAnimation(fadeInAnimation);
    }
}
