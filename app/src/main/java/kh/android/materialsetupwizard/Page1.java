package kh.android.materialsetupwizard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kh.android.materialwizard.PageFragment;
import kh.android.materialwizard.ProgressFragment;

/**
 * Created by liangyuteng0927 on 17-1-17.
 * Email: liangyuteng12345@gmail.com
 */

public class Page1 extends PageFragment implements View.OnClickListener{
    private String mTitle = "Page1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View view = inflater.inflate(R.layout.page1, viewGroup, false);
        view.findViewById(R.id.button_show_progress).setOnClickListener(this);
        view.findViewById(R.id.button_collapse_app_bar).setOnClickListener(this);
        view.findViewById(R.id.button_expand_app_bar).setOnClickListener(this);
        view.findViewById(R.id.button_swipe).setOnClickListener(this);
        view.findViewById(R.id.button_change_title).setOnClickListener(this);
        view.findViewById(R.id.button_restore_title).setOnClickListener(this);
        view.findViewById(R.id.button_change_header).setOnClickListener(this);
        view.findViewById(R.id.button_restore_header).setOnClickListener(this);
        return view;
    }

    @Override
    public String getTitle(Context context) {
        return mTitle;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_show_progress :
                getWizardActivity().turnTempPage(new ProgressFragment());
                break;
            case R.id.button_collapse_app_bar :
                getWizardActivity().setAppBarExpanded(false);
                break;
            case R.id.button_expand_app_bar :
                getWizardActivity().setAppBarExpanded(true);
                break;
            case R.id.button_swipe :
                getWizardActivity().setEnableSwipe(
                        !getWizardActivity().getEnableSwipe()
                );
                break;
            case R.id.button_change_title :
                mTitle = "Ha?";
                getWizardActivity().updateTitle();
                break;
            case R.id.button_restore_title :
                mTitle = "Page1";
                getWizardActivity().updateTitle();
                break;
            case R.id.button_change_header :
                setTitleBackgroundImage(ContextCompat.getDrawable(getWizardActivity(), R.drawable.common_setup_wizard_illustration_generic_wide));
                getWizardActivity().updateTitle();
                break;
            case R.id.button_restore_header :
                setTitleBackgroundImage(null);
                getWizardActivity().updateTitle();
                break;
        }
    }
}
