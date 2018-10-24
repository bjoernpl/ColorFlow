package com.bnpgames.android.colorflow.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bnpgames.android.colorflow.Helpers.PurchaseHelper;
import com.bnpgames.android.colorflow.R;
import com.bnpgames.android.colorflow.Statistics.Highscore;
import com.bnpgames.android.colorflow.Statistics.PointsHandler;

import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class StartFragment extends Fragment {

    private OnButtonPressedListener mListener;
    @BindView(R.id.highscore_text)
    TextView highscoretext;
    @BindView(R.id.totalScoreText)
    TextView totalScoreText;
    @BindView(R.id.title_text_premium)
    TextView premiumText;
    @BindView(R.id.premiumText)
    TextView getPremiumText;
    @BindView(R.id.colorsText) TextView colorsText;
    private Observer highScoreObserver;
    private int highScoreClickCounter = 0;

    public enum StartButton{
        Start,
        Colors,
        Settings,
        Premium
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
        if(PurchaseHelper.hasPremium(getActivity())){
            premiumText.setVisibility(View.VISIBLE);
            getPremiumText.setVisibility(View.GONE);
        }
        setHighscoreText(PointsHandler.getInstance().getHighscore(getActivity()));
        highScoreObserver = (observable, o) -> {
            if (o instanceof Highscore) setHighscoreText((Highscore) o);
            else setTotalScoreText((int)o);
        };
        PointsHandler.getInstance().addObserver(highScoreObserver);
        PointsHandler.getInstance().loadPoints(getActivity());
        return view;
    }

    private void setTotalScoreText(int o) {
        if(isAdded()) totalScoreText.setText(o+"");
    }

    private void setHighscoreText(Highscore score){
        if(isAdded()) highscoretext.setText(String.format(getString(R.string.highscore_text),score.getLevel(),score.getScore()));
    }

    @OnClick(R.id.highscore_text)
    void onHighScoreClicked(){
        if(highScoreClickCounter++>=5){
            colorsText.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.startplaying_text,R.id.colorsText,R.id.premiumText,R.id.imageButton})
    public void modeClicked(View view){
        switch (view.getId()){
            case R.id.startplaying_text:
                onButtonPressed(StartButton.Start);
                break;
            case R.id.colorsText:
                onButtonPressed(StartButton.Colors);
                return;
            case R.id.premiumText:
                onButtonPressed(StartButton.Premium);
                break;
            case R.id.imageButton:
                onButtonPressed(StartButton.Settings);
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
        PointsHandler.getInstance().deleteObserver(highScoreObserver);
    }


    public interface OnButtonPressedListener {
        void onButtonPressed(StartButton button);
    }
}
