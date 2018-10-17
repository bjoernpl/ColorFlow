package com.bnpgames.android.Gradients.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bnpgames.android.Gradients.GameModes.Game;
import com.bnpgames.android.Gradients.R;
import com.bnpgames.android.Gradients.Statistics.Highscore;
import com.bnpgames.android.Gradients.Statistics.PointsHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StartFragment extends Fragment {

    private OnButtonPressedListener mListener;
    @BindView(R.id.highscore_text)
    TextView highscoretext;
    @BindView(R.id.totalScoreText)
    TextView totalScoreText;

    public enum StartButton{
        Start,
        Profile,
        Settings
    }

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.start_screen, container, false);
        ButterKnife.bind(this,view);
        setHighscoreText(PointsHandler.getInstance().getHighscore(getActivity()));
        PointsHandler.getInstance().addObserver((observable, o) -> {
            if (o instanceof Highscore) setHighscoreText((Highscore) o);
        });
        PointsHandler.getInstance().addObserver((observable, o) -> {
            if(!(o instanceof Highscore)){
                totalScoreText.setText(o+"");
            }
        });
        PointsHandler.getInstance().loadPoints(getActivity());
        return view;
    }

    private void setHighscoreText(Highscore score){
        highscoretext.setText(String.format(getString(R.string.highscore_text),score.getLevel(),score.getScore()));
    }

    @OnClick({R.id.startplaying_text,R.id.profileText,R.id.settingsText})
    public void modeClicked(View view){
        switch (view.getId()){
            case R.id.startplaying_text:
                onButtonPressed(StartButton.Start);
                break;
            case R.id.profileText:
                onButtonPressed(StartButton.Profile);
                return;
            case R.id.settingsText:
                onButtonPressed(StartButton.Settings);
                break;
            default:

                break;
        }
    }

    public void onButtonPressed(StartButton button) {
        if (mListener != null) {
            mListener.onButtonPressed(button);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonPressedListener) {
            mListener = (OnButtonPressedListener) context;
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


    public interface OnButtonPressedListener {
        void onButtonPressed(StartButton button);
    }
}
