package com.example.yurii.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yurii.database.DBLessonConfig;
import com.example.yurii.speakeasy.CommonPage;
import com.example.yurii.speakeasy.StartUpActivity;

public class LessonsFragment extends Fragment {
    private static String TAG = "LessonFragment";
    private StartUpActivity mediator_;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mediator_ = (StartUpActivity) activity;
    }

    public static LessonsFragment newInstance() {
        LessonsFragment lessonFragment = new LessonsFragment();

        return lessonFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonPage page       = new CommonPage(inflater, this);
        DBLessonConfig config = new DBLessonConfig(getContext(), 0);

        page.setOnclickLessons(config.getLessonsList(), mediator_);

        return page.getMainView();
    }
}
