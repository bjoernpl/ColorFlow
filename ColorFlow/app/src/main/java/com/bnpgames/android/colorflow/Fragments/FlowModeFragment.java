package com.bnpgames.android.colorflow.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.bnpgames.android.colorflow.GameModes.ColorFlow;
import com.bnpgames.android.colorflow.GameModes.ColorFlowRadial;
import com.bnpgames.android.colorflow.GameModes.Game;
import com.bnpgames.android.colorflow.Levels.LevelRandomizer;
import com.bnpgames.android.colorflow.R;
import com.bnpgames.android.colorflow.Colors.ColorHandler;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlowModeFragment extends Fragment {

    private StartGameListener mListener;

    @BindView(R.id.colorFlow)
    ColorFlow colorFlow;

    @BindView(R.id.colorFlowRadial)
    ColorFlowRadial colorFlowRadial;

    @BindView(R.id.radioGroupGameChooser)
    RadioGroup flowModeChooser;

    private Game.FlowMode flowMode = Game.FlowMode.Linear;

    public FlowModeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_flow_mode, container, false);
        ButterKnife.bind(this,view);
        flowModeChooser.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (radioGroup.getCheckedRadioButtonId()){
                case R.id.radioButton:
                    flowMode = Game.FlowMode.Linear;
                    showLinear();
                    break;
                case R.id.radioButton2:
                    flowMode = Game.FlowMode.Radial;
                    showRadial();
                    break;
                case R.id.radioButton3:
                    flowMode = Game.FlowMode.Random;
                    if(new Random().nextBoolean()&&colorFlow.getVisibility()==View.GONE){
                        showLinear();
                    }else{
                        showRadial();
                    }
                    break;
            }
        });
        return view;
    }

    private void showLinear(){
        colorFlow.setVisibility(View.VISIBLE);
        colorFlowRadial.setVisibility(View.GONE);
        colorFlow.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(getActivity())));
        colorFlow.start();
    }

    private void showRadial(){
        colorFlow.setVisibility(View.GONE);
        colorFlowRadial.setVisibility(View.VISIBLE);
        colorFlowRadial.setLevel(LevelRandomizer.getStartLevel(ColorHandler.getColors(getActivity())));
        colorFlowRadial.start();
    }



    @OnClick(R.id.nextButton)
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onStartPressed(flowMode);
        }
    }

    @OnClick(R.id.backTextView)
    void onBackPressed(){
        getActivity().onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StartGameListener) {
            mListener = (StartGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface StartGameListener {
        void onStartPressed(Game.FlowMode flowMode);
    }
}
