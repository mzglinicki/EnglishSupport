package com.project.mzglinicki.yourowndictionary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.mzglinicki.yourowndictionary.R;
import com.project.mzglinicki.yourowndictionary.activities.MainActivity;

/**
 * Created by mzglinicki.96 on 27.08.2016.
 */
public class CreditsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_credits, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setToolbarTitle("O aplikacji");
    }
}