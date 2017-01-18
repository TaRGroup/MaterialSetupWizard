package kh.android.materialwizard;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by liangyuteng0927 on 17-1-17.
 * Email: liangyuteng12345@gmail.com
 */

public abstract class WizardActivity extends AppCompatActivity {
    ViewPager mViewPager;
    TextView mTextViewTitle;
    List<PageFragment> mPages;
    Adapter mPagerAdapter;
    private Button mButtonForward;
    private Button mButtonNext;
    private boolean mAutoUpdateButton = true;
    private PageFragment mTempPage;
    private RelativeLayout mAppBar;
    private View mAppBarText;
    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTextViewTitle = (TextView) findViewById(R.id.setupTextView1);
        mAppBar = (RelativeLayout)findViewById(R.id.appbar);
        mAppBarText = findViewById(R.id.include_app_bar_text);
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
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int p1, float p2, int p3) {
            }

            @Override
            public void onPageSelected(int p1) {
                updateButton();
                AnimationUtil.changeText(mPages.get(p1).getTitle(), mTextViewTitle);

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

    /* API */
    /**
     * Add a page
     * @param fragment Page
     */
    public void addPage (PageFragment fragment) {
        mPages.add(fragment);
        mPagerAdapter.notifyDataSetChanged();
        updateButton();
    }

    /**
     * Remove selected page by fragment
     * @param fragment Page
     */
    public void removePage (PageFragment fragment) {
        mPages.remove(fragment);
        mPagerAdapter.notifyDataSetChanged();
        updateButton();
    }

    /**
     * Remove selected page by index
     * @param pos Index
     */
    public void removePage (int pos) {
        mPages.remove(pos);
        mPagerAdapter.notifyDataSetChanged();
        updateButton();
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
        // TODO: Add fade animation
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout);
        if (expanded) {
            mAppBar.setVisibility(View.GONE);
            layout.addView(mAppBarText);
        } else {
            layout.removeView(mAppBarText);
            mAppBar.setVisibility(View.VISIBLE);
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
        AnimationUtil.fadeIn(findViewById(R.id.frame_temp), 200, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                AnimationUtil.changeText(fragment.getTitle(), mTextViewTitle);
                AnimationUtil.fadeOut(mViewPager, 200, new Animation.AnimationListener() {
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
                });
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
        });
    }

    /**
     * Dismiss temp page
     */
    public void dismissTempPage () {
        if (mTempPage == null)
            return;
        // TODO:以下这段100%掉坑注意
        AnimationUtil.fadeOut(findViewById(R.id.frame_temp), 200, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                AnimationUtil.changeText(mPages.get(mViewPager.getCurrentItem()).getTitle(), mTextViewTitle);
                mTempPage = null;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationUtil.fadeIn(mViewPager, 200, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mViewPager.setVisibility(View.VISIBLE);
                        setNextVisibility(View.VISIBLE);
                        setForwardVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                findViewById(R.id.frame_temp).setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .remove(mTempPage)
                        .commitAllowingStateLoss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // TODO:以上这段100%掉坑注意
        // 等能测试了再改吧…
    }
}
