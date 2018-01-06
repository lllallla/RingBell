package edu.fudan.ringbell.fragments;

/**
 * Created by niuzhenghao on 2017/11/27.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TabHost;

import edu.fudan.ringbell.R;


public class MyRingFragment extends Fragment{

    private RadioGroup mr_tab;//顶部选择框
    private MyRingFragmentController controller;

    public MyRingFragment() {
            // Required empty public constructor
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_myring, container, false);
            controller = MyRingFragmentController.getInstance(this, R.id.id_fragment);
            controller.showFragment(0);
            mr_tab = (RadioGroup) view.findViewById(R.id.mr_tab);
            mr_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.mr_word:
                            controller.showFragment(0);
                            break;
                        case R.id.mr_singer:
                            controller.showFragment(1);
                            break;
                        case R.id.mr_time:
                            controller.showFragment(2);
                            break;
                        case R.id.mr_col:
                            controller.showFragment(3);
                            break;
                        default:
                            break;
                    }
                }
            });

            return view;
        }
}
