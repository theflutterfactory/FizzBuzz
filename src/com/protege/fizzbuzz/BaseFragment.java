package com.protege.fizzbuzz;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by juliancurrie on 6/8/14.
 */
public class BaseFragment extends Fragment {

    FragmentActivity activity;

    @Override
    public void onAttach(Activity parentActivity){
        super.onAttach(parentActivity);
        activity = (FragmentActivity) parentActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

}
