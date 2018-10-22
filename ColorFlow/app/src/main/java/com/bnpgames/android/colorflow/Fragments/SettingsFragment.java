package com.bnpgames.android.colorflow.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.bnpgames.android.colorflow.Helpers.MusicHelper;
import com.bnpgames.android.colorflow.Helpers.PurchaseHelper;
import com.bnpgames.android.colorflow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingsFragment extends Fragment {

    private OnSettingsInteractionListener mListener;
    @BindView(R.id.music_switch) Switch switchView;
    @BindView(R.id.disable_ads_button)
    TextView disableAds;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this,view);
        if(PurchaseHelper.hasPremium(getActivity())){
            disableAds.setVisibility(View.GONE);
        }
        switchView.setChecked(MusicHelper.getInstance(getActivity()).isMusicEnabled(getActivity()));
        return view;
    }

    @OnCheckedChanged(R.id.music_switch)
    public void onSwitchChanged(Switch switchView){
        if(switchView.isChecked())MusicHelper.getInstance(getActivity()).setMusicEnabled(getActivity());
        else MusicHelper.getInstance(getActivity()).setMusicDisabled(getActivity());
    }

    @OnClick({R.id.credits_button,R.id.disable_ads_button,R.id.backTextView})
    public void onButtonPressed(View view) {
        if (mListener != null) {
            switch (view.getId()){
                case R.id.credits_button:
                    mListener.onCreditsClicked();
                    break;
                case R.id.disable_ads_button:
                    mListener.onDisableAdsClicked();
                    break;
                case R.id.backTextView:
                    mListener.onBackPressedFromSettings();
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsInteractionListener) {
            mListener = (OnSettingsInteractionListener) context;
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

    public interface OnSettingsInteractionListener {
        void onCreditsClicked();
        void onDisableAdsClicked();
        void onBackPressedFromSettings();
    }
}
