package edu.fudan.ringbell.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import edu.fudan.ringbell.LoginActivity;
import edu.fudan.ringbell.MainActivity;
import edu.fudan.ringbell.MakeRIngActivity;
import edu.fudan.ringbell.R;
import android.content.Intent;

/**
 * Created by niuzhenghao on 2017/11/27.
 */

public class MakeRingFragment extends Fragment {
    private FragmentManager manager;
    private FragmentTransaction ft;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflater.inflate(resource, null);
        super.onCreate(savedInstanceState);

        manager = getFragmentManager();
        View view = inflater.inflate(R.layout.fragment_makering, container, false);
        Button mrBymusic = (Button) view.findViewById(R.id.mrBymusic);
        Button mrByvoice = (Button) view.findViewById(R.id.mrByvoice);
        Button mrByrandom = (Button) view.findViewById(R.id.mrByrandom);

        mrBymusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), MakeRIngActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
