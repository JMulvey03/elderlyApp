package com.example.appforelderlyprotoelec;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;


public class sunnyFragment extends Fragment implements View.OnClickListener {

    Button sunnyActivity1,sunnyActivity2, sunnyActivity3, sunnyActivity4, sunnyActivity5;
    NestedScrollView sv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_sunny, container, false);
        sv= (NestedScrollView) view.findViewById(R.id.scrollViewSunny);
        sunnyActivity1=(Button)sv.findViewById(R.id.sunnyactivity1);
        sunnyActivity2=(Button)sv.findViewById(R.id.activity2);
        sunnyActivity3=(Button)sv.findViewById(R.id.activity3);
        sunnyActivity4=(Button)sv.findViewById(R.id.activity4);
        sunnyActivity5=(Button)sv.findViewById(R.id.activity5);
        sunnyActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),WalkRoute.class );
                startActivity(intent);
            }
        });
        sunnyActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),WalkRoute.class );
                startActivity(intent);

            }
        });
        sunnyActivity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),WalkRoute.class );
                startActivity(intent);

            }
        });
        sunnyActivity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),WalkRoute.class );
                startActivity(intent);
            }
        });
        sunnyActivity5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(),Routine_upperbody.class );
                startActivity(intent);
            }
        });
        return view;

    }


    @Override
    public void onClick(View view) {

    }
}