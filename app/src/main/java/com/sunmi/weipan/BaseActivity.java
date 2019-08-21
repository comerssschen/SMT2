package com.sunmi.weipan;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    private final int container = R.id.container;
    private ArrayList<BaseFragment> fragments;// back fragment list.
    private BaseFragment fragment;// current fragment.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * replace the current fragment.
     *
     * @param fragment       the new fragment to shown.
     * @param addToBackStack if it can back.
     */
    public void addContent(BaseFragment fragment, boolean addToBackStack) {
        initFragments();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        } else {
            removePrevious();
        }

        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
        fragments.add(fragment);
        setFragment();
    }

    // use replace method to show fragment.
    public void replaceContent(BaseFragment fragment, boolean addToBackStack) {
        initFragments();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        } else {
            removePrevious();
        }
        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();

        fragments.add(fragment);
        setFragment();
    }


    /**
     * set current fragment.
     */
    private void setFragment() {
        if (fragments != null && fragments.size() > 0) {
            fragment = fragments.get(fragments.size() - 1);
        } else {
            fragment = null;
        }
    }

    /**
     * remove previous fragment
     */
    private void removePrevious() {
        if (fragments != null && fragments.size() > 0) {
            fragments.remove(fragments.size() - 1);
        }
    }

    /**
     * init fragment list.
     */
    private void initFragments() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
    }

}
