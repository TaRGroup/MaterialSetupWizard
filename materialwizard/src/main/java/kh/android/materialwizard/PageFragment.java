package kh.android.materialwizard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liangyuteng0927 on 17-1-17.
 * Email: liangyuteng12345@gmail.com
 */

public abstract class PageFragment extends Fragment {
    private Drawable mTitleBackgroundImage;

    public Drawable getTitleBackgroundImage () {
        return mTitleBackgroundImage;
    }

    @Override
    public abstract View onCreateView (LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle);

    /* API */
    /**
     * Set page title
     * @return Page Title
     */
    public abstract String getTitle (Context context);

    /**
     * Set title background image
     * @param drawable Image, if equals null, will set default image.
     */
    public void setTitleBackgroundImage (Drawable drawable) {
        if (drawable == null) {
            mTitleBackgroundImage = getResources().getDrawable(R.mipmap.common_setup_wizard_illustration_generic);
        } else {
            mTitleBackgroundImage = drawable;
        }
    }

    /**
     * Get attached wizard activity, easily to control wizard.
     * @return Wizard activity
     * @throws IllegalArgumentException If this fragment not attached to WizardActivity
     */
    public WizardActivity getWizardActivity () throws IllegalArgumentException{
        return (WizardActivity)getActivity();
    }
}
