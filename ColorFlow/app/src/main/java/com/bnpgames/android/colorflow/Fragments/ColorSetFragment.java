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
import com.bnpgames.android.colorflow.Helpers.ColorSetListSerializer;
import com.bnpgames.android.colorflow.Helpers.InterfaceHelper;
import com.bnpgames.android.colorflow.Levels.LevelHandler;
import com.bnpgames.android.colorflow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ColorSetFragment extends Fragment implements InterfaceHelper.ColorSelectedListener {

    @BindView(R.id.colorsRV)
    RecyclerView colors;
    private InterfaceHelper.ColorSelectedListener listener;


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
        ColorsAdapter adapter = new ColorsAdapter(getActivity(),this);
        colors.setAdapter(adapter);
        colors.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onColorSetSelected(ColorHandler.ColorSet colorSet) {
        ColorHandler.setSelectedColorSet(getActivity(), colorSet);
        ColorSetListSerializer.addSelectedSet(getActivity(),colorSet);
        LevelHandler.getInstance().setColors(getActivity());
        LevelHandler.getInstance().reset();
        listener.onColorSetSelected(colorSet);
    }

    @Override
    public void onColorUnselected(ColorHandler.ColorSet colorSet) {
        ColorSetListSerializer.removeSelectedSet(getActivity(),colorSet);
    }

    @OnClick(R.id.backTextView)
    void onBackPressed(){
        getActivity().onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceHelper.ColorSelectedListener) {
            listener = (InterfaceHelper.ColorSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ColorSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }



}
