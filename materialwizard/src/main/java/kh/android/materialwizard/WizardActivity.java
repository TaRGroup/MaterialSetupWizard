package kh.android.materialwizard;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangyuteng0927 on 17-1-17.
 * Email: liangyuteng12345@gmail.com
 */

public abstract class WizardActivity extends AppCompatActivity {
    WizardViewPager mViewPager;
    TextView mTextViewTitle;
    List<PageFragment> mPages;
    Adapter mPagerAdapter;
    private Button mButtonForward;
    private Button mButtonNext;
    private boolean mAutoUpdateButton = true;
    private PageFragment mTempPage;
    private FrameLayout mAppBar;
    private RelativeLayout mAppBarText;
    private ImageView mHeader;
    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.WizardStyle);
        setContentView(R.layout.setup);
        mViewPager = (WizardViewPager) findViewById(R.id.pager);
        mTextViewTitle = (TextView) findViewById(R.id.setupTextView1);
        mAppBar = (FrameLayout)findViewById(R.id.appbar);
        mAppBarText = (RelativeLayout)findViewById(R.id.include_app_bar_text);
        mHeader = (ImageView)findViewById(R.id.header);
        mPages = new ArrayList<>();
        mButtonForward = (Button)findViewById(R.id.buttonForward);
        mButtonNext = (Button)findViewById(R.id.buttonNext);
        mButtonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() > 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                }
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() < mPages.size() - 1) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                } else {
                    finish();
                }
            }
        });
        mPagerAdapter = new Adapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(new WizardViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int p1, float p2, int p3) {
            }

            @Override
            public void onPageSelected(int p1) {
                updateButton();
                updateTitle(p1);
            }

            @Override
            public void onPageScrollStateChanged(int p1) {
            }
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    private class Adapter extends FragmentPagerAdapter {
        Adapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return mPages.get(position);
        }

        @Override
        public int getCount() {
            return mPages.size();
        }
    }
    private void updateButton () {
        int p1 = mViewPager.getCurrentItem();
        if (p1 == 0) {
            setForwardVisibility(View.INVISIBLE);
        } else {
            setForwardVisibility(View.VISIBLE);
        }
        if (p1 == mPages.size() - 1) {
            setNextText(getString(R.string.setup_step_next_final));
        } else {
            setNextText(getString(R.string.setup_step_next));
        }
    }
    @Override
    public void onBackPressed () {
        if (mTempPage != null) {
            return;
        }
        if (getForwardVisibility() == View.INVISIBLE ||
                getForwardVisibility() == View.GONE ||
                !getForwardEnable()) {
            if (getCurrentPage()  != 0)
                return;
        }
        if (getCurrentPage() != 0) {
            turnPage(getCurrentPage() - 1);
            return;
        }
        super.onBackPressed();
    }
    private void updateTitle (int index) {
        updateTitle(mPages.get(index));
    }
    private void updateTitle (PageFragment fragment) {
        changeHeaderImage(fragment.getTitleBackgroundImage());
        AnimationUtil.changeText(fragment.getTitle(WizardActivity.this), mTextViewTitle);
    }
    private void reset () {
        changeHeaderImage(null);
        AnimationUtil.changeText(getString(R.string.app_name), mTextViewTitle);
    }

    private void changeHeaderImage (final Drawable drawable) {
        // 判断drawable是否变化。会闪烁白色
        // 因为你没淡出啊…
        AnimationUtil.fade(mHeader, 100, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (drawable == null) {
                            mHeader.setImageResource(R.mipmap.common_setup_wizard_illustration_generic);
                        } else {
                            mHeader.setImageDrawable(drawable);
                        }
                        AnimationUtil.fade(mHeader, 100, null, 0.75f, 1);
                    }
                }, 50);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        },1,0.75f);
    }

    /* API */
    /**
     * Add a page
     * @param fragment Page
     */
    public void addPage (PageFragment fragment) {
        mPages.add(fragment);
        mPagerAdapter.notifyDataSetChanged();
        updateButton();
        if (mPages.size() == 1) {
            // This is first page.
            updateTitle(fragment);
        }
    }

    /**
     * Remove selected page by fragment
     * @param fragment Page
     */
    public void removePage (PageFragment fragment) {
        mPages.remove(fragment);
        mPagerAdapter.notifyDataSetChanged();
        updateButton();
        if (mPages.size() == 0) {
            // This is last page.
            reset();
        }
    }

    /**
     * Remove selected page by index
     * @param pos Index
     */
    public void removePage (int pos) {
        mPages.remove(pos);
        mPagerAdapter.notifyDataSetChanged();
        updateButton();
        if (mPages.size() == 0) {
            // This is last page.
            reset();
        }
    }

    /**
     * Turn to selected page
     * @param pos Index
     */
    public void turnPage (int pos) {
        mViewPager.setCurrentItem(pos);
    }

    /**
     * Set forward button enable. You also can set enable to false to deny user click back key.
     * @param enable Enable
     */
    public void setForwardEnable (boolean enable) {
        mButtonForward.setEnabled(enable);
    }

    /**
     * Set forward button visibility. You also can set visibility to GONE to deny user click back key.
     * @param visibility Visibility
     */
    public void setForwardVisibility (int visibility) {
        mButtonForward.setVisibility(visibility);
    }

    /**
     * Set next button enable
     * @param enable Enable
     */
    public void setNextEnable (boolean enable) {
        mButtonNext.setEnabled(enable);
    }

    /**
     * Set next button visibility
     * @param visibility Visibility
     */
    public void setNextVisibility (int visibility) {
        mButtonNext.setVisibility(visibility);
    }

    /**
     * Get forward button enable
     */
    public boolean getForwardEnable () {
        return mButtonForward.isEnabled();
    }

    /**
     * Get forward button visibility
     */
    public int getForwardVisibility () {
        return mButtonForward.getVisibility();
    }

    /**
     * Get next button enable
     */
    public boolean getNextEnable () {
        return mButtonNext.isEnabled();
    }
    /**
     * Get next button visibility
     */
    public int getNextVisibility () {
        return mButtonNext.getVisibility();
    }

    /**
     * Button will auto update when turned First and Last page. You can check this function enable.
     * @return Enable
     */
    public boolean isAutoUpdateButton() {
        return mAutoUpdateButton;
    }

    /**
     * Button will auto update when turned First and Last page. You can control this function enable;
     * @param mAutoUpdateButton Enable
     */
    public void setAutoUpdateButton(boolean mAutoUpdateButton) {
        this.mAutoUpdateButton = mAutoUpdateButton;
    }

    /**
     * Set app bar expanded
     * @param expanded Expanded
     */
    public void setAppBarExpanded (boolean expanded) {
        /*
        if (expanded) {
            findViewById(R.id.header).setVisibility(View.GONE);
            ViewGroup.LayoutParams params = mAppBar.getLayoutParams();
            params.height = 70;
            mAppBar.setLayoutParams(params);
            if (Build.VERSION.SDK_INT >= 19) {
                View status = findViewById(R.id.status);
                status.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams layoutParams = status.getLayoutParams();
                Rect rectangle = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                int statusBarHeight = rectangle.top;
                int contentViewTop =
                        window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
                layoutParams.height = statusBarHeight;
                status.setLayoutParams(layoutParams);
            }
        } else {
            findViewById(R.id.header).setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = mAppBar.getLayoutParams();
            params.height = 244;
            mAppBar.setLayoutParams(params);
            findViewById(R.id.status).setVisibility(View.GONE);
        }+
        */
        // TODO: 重写
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout);
        boolean isMarshmallow = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        boolean isKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        int statusBarHeight = 0;
        if (isMarshmallow)
            statusBarHeight = 24;
        else if (isKitkat)
            statusBarHeight = 25;
        if (expanded) {
            mAppBar.removeView(mAppBarText);
            layout.addView(mAppBarText);
            mAppBar.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = mAppBarText.getLayoutParams();
            params.height = dpToPx(statusBarHeight + 64);
            mAppBarText.setLayoutParams(params);
            mAppBarText.findViewById(R.id.setupTextView1).setPadding(0,dpToPx(statusBarHeight),0,0);
        } else {
            layout.removeView(mAppBarText);
            mAppBar.addView(mAppBarText);
            mAppBar.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dpToPx(64));
            layoutParams.gravity = Gravity.BOTTOM;
            mAppBarText.setLayoutParams(layoutParams);
            mAppBarText.findViewById(R.id.setupTextView1).setPadding(0,0,0,0);
        }
    }

    /**
     * Set Forward button text, if text equals null, will set to default
     * @param text Button text
     */
    public void setForwardText (CharSequence text) {
        if (text == null) {
            mButtonForward.setText(R.string.setup_step_before);
            return;
        }
        mButtonForward.setText(text);
    }

    /**
     * Set Next button text, if text equals null, will set to default
     * @param text Button text
     */
    public void setNextText (CharSequence text) {
        if (text == null) {
            mButtonNext.setText(R.string.setup_step_next);
            return;
        }
        mButtonNext.setText(text);
    }

    /**
     * Get Forward button text
     * @return Button text, as default is "FORWARD"
     */
    public CharSequence getForwardText () {
        return mButtonForward.getText();
    }

    /**
     * Get Next button text
     * @return Button text, as default is "NEXT"
     */
    public CharSequence getNextText () {
        return mButtonNext.getText();
    }

    /**
     * Get pages list size
     * @return Size
     */
    public int getPagesCount () {
        return mPages.size();
    }

    /**
     * Get current page index
     * @return Index
     */
    public int getCurrentPage () {
        return mViewPager.getCurrentItem();
    }

    /**
     * Set button bar visibility
     * @param visibility Visibility
     */
    public void setButtonBarVisibility (int visibility) {
        findViewById(R.id.buttonBar).setVisibility(visibility);
    }

    /**
     * Get button bar visibility
     * @return Visibility
     */
    public int getButtonBarVisibility () {
        return findViewById(R.id.buttonBar).getVisibility();
    }

    /**
     * Turn a temp page, not add to list and call turn.
     * You only can show ONE temp page on screen.
     * Before show a temp page, back key will disabled.
     * @param fragment Page
     */
    public void turnTempPage (final PageFragment fragment) {
        findViewById(R.id.frame_temp).setVisibility(View.VISIBLE);
        AnimationUtil.fade(findViewById(R.id.frame_temp), 200, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                AnimationUtil.changeText(fragment.getTitle(WizardActivity.this), mTextViewTitle);
                AnimationUtil.fade(mViewPager, 200, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mViewPager.setVisibility(View.GONE);
                        setNextVisibility(View.INVISIBLE);
                        setForwardVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                },1,0);
                mTempPage = fragment;
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_temp, mTempPage)
                        .commitAllowingStateLoss();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        },0,1);
    }

    /**
     * Dismiss temp page
     */
    public void dismissTempPage () {
        if (mTempPage == null)
            return;
        // 以下这段100%掉坑注意
        AnimationUtil.fade(findViewById(R.id.frame_temp), 200, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mViewPager.setVisibility(View.VISIBLE);
                setNextVisibility(View.VISIBLE);
                setForwardVisibility(View.VISIBLE);
                String title = getString(R.string.app_name);
                try {
                    title = mPages.get(mViewPager.getCurrentItem())
                            .getTitle(WizardActivity.this);
                } catch (IndexOutOfBoundsException e) {}
                AnimationUtil.changeText(title, mTextViewTitle);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(mTempPage)
                        .commitAllowingStateLoss();
                mTempPage = null;
                findViewById(R.id.frame_temp).setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        },1,0);
        // 以上这段100%掉坑注意
        // 等能测试了再改吧…
    }

    /**
     * @param enable Enable viewpager swipe. As default is false.
     */
    public void setEnableSwipe (boolean enable) {
        mViewPager.setEnableSwipe(enable);
    }

    /**
     * Get is enable viewpager swipe
     * @return Is enable swipe
     */
    public boolean getEnableSwipe () {
        return mViewPager.getEnableSwipe();
    }

    /**
     * Update current page title
     */
    public void updateTitle () {
        updateTitle(mPages.get(getCurrentPage()));
    }

    /**
     * Turn dip into px.
     * ONLY FOR EXPANDING!
     * @param dp dip value
     * @return px value
     */
    private int dpToPx(int dp){
        DisplayMetrics displayMestrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMestrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
