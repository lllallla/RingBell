package edu.fudan.ringbell.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * Created by niuzhenghao on 2018/1/6.
 */


public class MyRingFragmentController {
    private int containerId;
    private FragmentManager fm;
    private ArrayList<Fragment> fragments;

    private static MyRingFragmentController controller;

    public static MyRingFragmentController getInstance(MyRingFragment parentFragment, int containerId) {
        if (controller == null) {
            controller = new MyRingFragmentController(parentFragment, containerId);
        }
        return controller;
    }

    private MyRingFragmentController(Fragment fragment, int containerId) {
        this.containerId = containerId;
        //fragment嵌套fragment，调用getChildFragmentManager
        fm = fragment.getChildFragmentManager();

        initFragment();
    }

    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new MusicByWordFragment());
        fragments.add(new MusicBySingerFragment());
        fragments.add(new MusicByTimeFragment());
        fragments.add(new MusicByColFragment());

        FragmentTransaction ft = fm.beginTransaction();
        for(Fragment fragment : fragments) {
            ft.add(containerId, fragment);
        }
        ft.commit();
    }

    public void showFragment(int position) {
        hideFragments();
        Fragment fragment = fragments.get(position);
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public void hideFragments() {
        FragmentTransaction ft = fm.beginTransaction();
        for(Fragment fragment : fragments) {
            if(fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }
}
