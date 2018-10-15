package com.example.android.colorflow.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.colorflow.GameModes.Game;
import com.example.android.colorflow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SpeedModeFragment extends Fragment {

    private OnSpeedModeSelectedListener mListener;
    @BindView(R.id.radioGroupDifficultyChooser) RadioGroup difficultyChoice;
    private Game.Difficulty difficulty;

    public SpeedModeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.speed_mode_layout, container, false);
        ButterKnife.bind(this,view);
        difficultyChoice.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.radioButton:
                    difficulty = Game.Difficulty.Easy;
                    break;
                case R.id.radioButton2:
                    difficulty = Game.Difficulty.Medium;
                    break;
                case R.id.radioButton3:
                    difficulty = Game.Difficulty.Hard;
                    break;
            }
        });
        return view;
    }

    @OnClick({R.id.time_choice_10_seconds,R.id.time_choice_30_seconds,R.id.time_choice_60_seconds})
    public void timeClicked(View view) {
        if(difficulty==null){
            Toast.makeText(getActivity(),"Please choose a difficulty first",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()){
            case R.id.time_choice_10_seconds:
                onTimeAndDifficultySelected(difficulty,10);
                break;
            case R.id.time_choice_30_seconds:
                onTimeAndDifficultySelected(difficulty,30);
                break;
            case R.id.time_choice_60_seconds:
                onTimeAndDifficultySelected(difficulty,60);
                break;
        }
    }

    public void onTimeAndDifficultySelected(Game.Difficulty difficulty, int time) {
        if (mListener != null) {
            mListener.onSpeedModeSelected(difficulty,time);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSpeedModeSelectedListener) {
            mListener = (OnSpeedModeSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSpeedModeSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnSpeedModeSelectedListener {
        void onSpeedModeSelected(Game.Difficulty difficulty, int time);
    }
}
