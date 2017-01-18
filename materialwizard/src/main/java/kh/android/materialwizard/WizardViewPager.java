package kh.android.materialwizard;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liangyuteng0927 on 17-1-18.
 * Email: liangyuteng12345@gmail.com
 * A custom view pager can disable swipe.
 * <a href="http://stackoverflow.com/questions/7814017/is-it-possible-to-disable-scrolling-on-a-viewpager">See this</a>
 * @hide
 */
class WizardViewPager extends ViewPager {
    private boolean mEnableSwipe = false;

    public WizardViewPager(Context context) {
        super(context);
    }

    public WizardViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mEnableSwipe && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mEnableSwipe && super.onTouchEvent(event);
    }

    /**
     * @param enable Enable swipe
     */
    public void setEnableSwipe (boolean enable) {
        mEnableSwipe = enable;
    }

    /**
     * Get is enable swipe
     * @return Is enable swipe
     */
    public boolean getEnableSwipe () {
        return mEnableSwipe;
    }
}
