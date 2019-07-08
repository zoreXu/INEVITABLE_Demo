package edu.nuc.sporthealth.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class BaseFragment extends Fragment {


    public String TAG;

    protected Activity mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
