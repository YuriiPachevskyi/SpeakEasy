package com.example.yurii.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yurii.database.DBMaterial;
import com.example.yurii.speakeasy.CommonPage;
import com.example.yurii.speakeasy.StartUpActivity;

public class MaterialFragment extends Fragment {
    private static String TAG               = "MaterialFragment";
    private static String MATERIAL_FLAG_KEY = "TagFlagKey";
    private String        currentTag_;
    private StartUpActivity mediator_;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mediator_ = (StartUpActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentTag_ = getArguments().getString(MATERIAL_FLAG_KEY);
    }

    public static MaterialFragment newInstance(String tag) {
        MaterialFragment materialFragment = new MaterialFragment();
        Bundle bundle                     = new Bundle(1);

        bundle.putString(MATERIAL_FLAG_KEY, tag);
        materialFragment.setArguments(bundle);

        return materialFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonPage page     = new CommonPage(inflater, this);
        DBMaterial material = new DBMaterial(getContext(), 0);

        int position = 0;

        if ( mediator_.getLessonFragment() != null ) {
            position = mediator_.getLessonFragment().getSlidingTabLayout().getSelectedTabPosition();
        }

        page.setMaterial(currentTag_, material, position);

        return  page.getMainView();
    }
}
