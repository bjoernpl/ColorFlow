package com.bnpgames.android.colorflow.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnpgames.android.colorflow.Colors.ColorHandler;
import com.bnpgames.android.colorflow.Colors.ColorsAdapter;
import com.bnpgames.android.colorflow.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ColorSetFragment extends Fragment {

    @BindView(R.id.colorsRV)
    RecyclerView colors;


    public ColorSetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_color_set, container, false);
        ButterKnife.bind(this,view);
        ColorsAdapter adapter = new ColorsAdapter(getActivity());
        colors.setAdapter(adapter);
        colors.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }


}
