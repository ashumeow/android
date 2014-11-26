package org.sagemath.droid.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import org.sagemath.droid.R;
import org.sagemath.droid.fragments.ManageInsertFragment;

/**
 * The activity that hosts the Insert Fragment
 *
 * @author Nikhil Peter Raj
 */
public class ManageInsertActivity extends ActionBarActivity {
    private static final String TAG = "SageDroid:ManageInsertActivity";

    private ManageInsertFragment insertFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_insert);

    }
}
