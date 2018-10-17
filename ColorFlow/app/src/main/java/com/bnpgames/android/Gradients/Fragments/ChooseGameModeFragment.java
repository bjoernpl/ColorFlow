package com.bnpgames.android.Gradients.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnpgames.android.Gradients.GameModes.Game;
import com.bnpgames.android.Gradients.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChooseGameModeFragment extends Fragment {

    private OnGameModeChosenListener mListener;

    public ChooseGameModeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.backTextView)
    public void backPressed(){
        getActivity().onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_game_mode_select2, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.rl_casual_mode,R.id.rl_speed_mode,R.id.rl_continuous_mode,R.id.rl_endurance_mode})
    public void modeClicked(View view){
        switch (view.getId()){
            case R.id.rl_casual_mode:
                onModeChosen(Game.GameMode.Casual);
                break;
            case R.id.rl_speed_mode:
                onModeChosen(Game.GameMode.Speed);
                return;
            case R.id.rl_continuous_mode:
                onModeChosen(Game.GameMode.Continuous);
                break;
            case R.id.rl_endurance_mode:
                onModeChosen(Game.GameMode.Endurance);
                break;
            default:

                break;
        }
    }

    public void onModeChosen(Game.GameMode mode) {
        if (mListener != null) {
            mListener.onModeChosen(mode);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGameModeChosenListener) {
            mListener = (OnGameModeChosenListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnGameModeChosenListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnGameModeChosenListener {
        void onModeChosen(Game.GameMode mode);
    }
}
