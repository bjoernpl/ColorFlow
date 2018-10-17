package com.bnpgames.android.Gradients.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bnpgames.android.Gradients.Levels.GameFinished;
import com.bnpgames.android.Gradients.R;
import com.bnpgames.android.Gradients.Statistics.Highscore;

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

    private GameFinished gameFinished;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.excpectedColorView) View expectedColorView;
    @BindView(R.id.you_pressed_View) View actualColorView;
    @BindView(R.id.totalScoreText) TextView totalScoreText;
    @BindView(R.id.levelText) TextView levelIndexText;
    @BindView(R.id.highScoreText) View highScoreText;


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
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_failed,container,false);
        ButterKnife.bind(this,view);
        actualColorView.setBackgroundColor(gameFinished.getActualColor());
        expectedColorView.setBackgroundColor(gameFinished.getExpectedColor());
        totalScoreText.setText(String.format("TOTAL SCORE: %d",gameFinished.getScore().getScore()));
        levelIndexText.setText(String.format("LEVEL %d",gameFinished.getScore().getLevel()));
        highScoreText.setVisibility(gameFinished.isHighScore() ? View.VISIBLE : View.GONE);
        return view;
    }

    @OnClick({R.id.nextButton,R.id.backTextView})
    public void onButtonPressed(View view) {
        if (mListener != null) {
            if(view.getId()==R.id.nextButton)mListener.onPlayAgainClicked();
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
                    + " must implement OnFragmentInteractionListener");
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
