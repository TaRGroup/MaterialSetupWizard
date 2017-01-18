package kh.android.materialsetupwizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import kh.android.materialwizard.AnimationUtil;
import kh.android.materialwizard.ProgressFragment;

/**
 * Created by liangyuteng0927 on 17-1-17.
 * Email: liangyuteng12345@gmail.com
 */

public class ProgressFragmentTemp extends ProgressFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        final View view = super.onCreateView(inflater, viewGroup, bundle);
        android.widget.Button dismissButton = (android.widget.Button)view.findViewById(R.id.dismiss);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWizardActivity().dismissTempPage();
            }
        });
        return view;
    }
}
