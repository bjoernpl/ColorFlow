package com.bnpgames.android.colorflow.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bnpgames.android.colorflow.GameModes.Game;
import com.bnpgames.android.colorflow.Levels.GameFinished;
import com.bnpgames.android.colorflow.R;
import com.bnpgames.android.colorflow.Statistics.PointsHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FailedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FailedFragment extends Fragment {
    // the fragment initialization parameters
    private static final String EXPECTED_COLOR = "expected";
    private static final String ACTUAL_COLOR = "actual";
    private static final String TOTAL_SCORE = "total";
    private static final String IS_HIGHSCORE = "highscore";
    private static final String FLOWMODE = "flowmode";

    private GameFinished gameFinished;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.totalScoreText) TextView totalScoreText;
    @BindView(R.id.failed_level_mode) TextView levelIndexText;
    @BindView(R.id.highScoreText) View highScoreText;
    @BindView(R.id.totalScoreAddition) TextView addedPoints;
    @BindView(R.id.failed_score) TextView scoreView;


    public FailedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FailedFragment newInstance(GameFinished gameFinished) {
        FailedFragment fragment = new FailedFragment();
        Bundle args = new Bundle();
        args.putInt(EXPECTED_COLOR, gameFinished.getExpectedColor());
        args.putInt(ACTUAL_COLOR, gameFinished.getActualColor());
        args.putParcelable(TOTAL_SCORE, gameFinished.getScore());
        args.putBoolean(IS_HIGHSCORE, gameFinished.isHighScore());
        args.putString(FLOWMODE,gameFinished.getFlowMode().name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameFinished = new GameFinished();
            gameFinished.setExpectedColor(getArguments().getInt(EXPECTED_COLOR));
            gameFinished.setActualColor(getArguments().getInt(ACTUAL_COLOR));
            gameFinished.setScore(getArguments().getParcelable(TOTAL_SCORE));
            gameFinished.setHighScore(getArguments().getBoolean(IS_HIGHSCORE));
            gameFinished.setFlowMode(Game.FlowMode.valueOf(getArguments().getString(FLOWMODE)));
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.failed_fragment,container,false);
        ButterKnife.bind(this,view);
        scoreView.setText(String.format("%d",gameFinished.getScore().getScore()));
        levelIndexText.setText(getString(R.string.level_mode,gameFinished.getScore().getLevel(),gameFinished.getFlowMode().name().toUpperCase()));
        highScoreText.setVisibility(gameFinished.isHighScore() ? View.VISIBLE : View.GONE);
        addedPoints.setText(String.format("+%d",gameFinished.getScore().getScore()));
        int scoreBefore = PointsHandler.getInstance().getTotalScore()-gameFinished.getScore().getScore();
        totalScoreText.setText(""+ scoreBefore);
        int[] colors = {gameFinished.getActualColor(),gameFinished.getExpectedColor()};
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,colors);
        view.setBackground(drawable);
        return view;
    }

    @SuppressLint("DefaultLocale")
    @OnClick({R.id.failed_share_score})
    void onSharePressed(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, String.format("I just got to level %d and scored %d points in Gradients %s Mode! Can you beat me? http://play.google.com/store/apps/details?id=com.bnpgames.android.colorflow",gameFinished.getScore().getLevel(),gameFinished.getScore().getScore(),gameFinished.getFlowMode().name()));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share with"));
    }

    @OnClick({R.id.failed_play_again,R.id.failed_back_to_menu})
    public void onButtonPressed(View view) {
        if (mListener != null) {
            if(view.getId()==R.id.failed_play_again)mListener.onPlayAgainClicked();
            else mListener.onBackClicked();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPlayAgainClicked();
        void onBackClicked();
    }
}
