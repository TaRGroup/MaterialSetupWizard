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
    public static void changeText (final String newText, final TextView textView) {
        AnimationSet as1=new AnimationSet(true);
        AlphaAnimation aa1=new AlphaAnimation(1,0);
        aa1.setDuration(200);
        as1.addAnimation(aa1);
        textView.startAnimation(as1);
        as1.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation p1)
            {
                // TODO: Implement this method
            }

            @Override
            public void onAnimationEnd(Animation p1)
            {
                textView.setVisibility(View.GONE);
                textView.setText(newText);
                AnimationSet as=new AnimationSet(true);
                AlphaAnimation aa=new AlphaAnimation(0,1);
                aa.setDuration(200);
                as.addAnimation(aa);
                textView.startAnimation(as);
                as.setAnimationListener(new Animation.AnimationListener(){

                    @Override
                    public void onAnimationStart(Animation p1) {
                    }

                    @Override
                    public void onAnimationEnd(Animation p1) {
                        textView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation p1) {
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation p1) {
            }
        });
    }
}
