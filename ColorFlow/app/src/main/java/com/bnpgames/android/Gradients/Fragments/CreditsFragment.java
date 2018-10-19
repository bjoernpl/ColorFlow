package com.bnpgames.android.Gradients.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnpgames.android.Gradients.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditsFragment extends Fragment {


    public CreditsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_credits, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.backTextView)
    void onBackPressed(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

}
