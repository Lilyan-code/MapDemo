package com.example.tangwenyan.map;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tangwenyan.map.Activity.NavigationActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main_Fragment extends Fragment {


    public Main_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Button button = (Button) rootView.findViewById(R.id.qrcode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*Intent intent = new Intent(getActivity(), NavigationActivity.class);
                startActivity(intent);*/
            }
        });
        return rootView;
    }

}
