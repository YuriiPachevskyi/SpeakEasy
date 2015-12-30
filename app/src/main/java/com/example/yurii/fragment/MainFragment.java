package com.example.yurii.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.example.yurii.speakeasy.R;

public class MainFragment extends Fragment {
    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    public static MainFragment newInstance(String text){
        MainFragment mFragment = new MainFragment();
//        Bundle mBundle = new Bundle();
//        mBundle.putString(TEXT_FRAGMENT, text);
//        mFragment.setArguments(mBundle);
        return mFragment;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//        Bundle savedInstanceState) {
//        Log.e("onCreateView +++ ", "test ---");
//        // TODO Auto-generated method stub
//        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//
//        TextView mTxtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
//        mTxtTitle.setText(getArguments().getString(TEXT_FRAGMENT));
//
//        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
//        return rootView;
//    }
}
