package kh.android.materialsetupwizard;

import android.os.Bundle;

import kh.android.materialwizard.ProgressFragment;
import kh.android.materialwizard.WizardActivity;

/**
 * Created by liangyuteng0927 on 17-1-17.
 * Email: liangyuteng12345@gmail.com
 */

public class Act extends WizardActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPage(new Page1());
        //addPage(new Page2());
        //addPage(new ProgressFragmentTemp());
        //turnPage(0);
        turnTempPage(new ProgressFragment());
    }
}
