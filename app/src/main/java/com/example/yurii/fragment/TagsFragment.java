package com.example.yurii.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yurii.database.DBMaterial;
import com.example.yurii.speakeasy.CommonPage;
import com.example.yurii.speakeasy.StartUpActivity;


public class TagsFragment extends Fragment {
    private static String TAG = "TagsFragment";
    StartUpActivity       mediator_;

    public static TagsFragment newInstance() {
        TagsFragment tagsFragment = new TagsFragment();

        return tagsFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mediator_ = (StartUpActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonPage page     = new CommonPage(inflater, this);
        DBMaterial material = new DBMaterial(getContext(), 0);

        page.setOnclickTags(material.getFullTagsList(), mediator_);

        return  page.getMainView();
    }
}
