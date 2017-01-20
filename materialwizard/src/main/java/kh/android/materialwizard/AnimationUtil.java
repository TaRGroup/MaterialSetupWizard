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
        fade(textView,200,new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation p1) {
                // Should do something, but this method is useless yet…
            }

            @Override
            public void onAnimationEnd(Animation p1) {
                textView.setText(newText);
                fade(textView,200,null,0,1);
            }

            @Override
            public void onAnimationRepeat(Animation p1) {
                // Google thinks that this method should also do something!
                // Why there are so many useless methods in the API?
            }
        },1,0);
    }
    /**
     * @author Rachel
     * @param v View you want to fade out
     * @param duration Time anim lasts
     * @param listener What do you want to do in the end or when anim starts?
     * @param from alpha when anim starts
     * @param to alpha when anim stops
     */
    static void fade(View v,long duration,Animation.AnimationListener listener,float from,float to){
        // Basic Animation definition
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(from,to);
        fadeOutAnimation.setDuration(duration);
        if (listener != null)
            fadeOutAnimation.setAnimationListener(listener);
        v.startAnimation(fadeOutAnimation);
    }
}
