package com.example.yurii.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yurii.database.DBExercise;
import com.example.yurii.database.DBHomework;
import com.example.yurii.database.DBLessonConfig;
import com.example.yurii.database.DBMaterial;
import com.example.yurii.database.DBSpeaking;
import com.example.yurii.page.LessonPage;
import com.example.yurii.speakeasy.CommonPage;
import com.example.yurii.speakeasy.StartUpActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeworkFragment extends Fragment {
    private static String TAG             = "HomeworkFragment";
    private static String LESSON_FLAG_KEY = "LessonFlagKey";
    private int           lesson_;
    StartUpActivity mediator_;

    public static HomeworkFragment newInstance(int lesson) {
        HomeworkFragment speakingFragment = new HomeworkFragment();
        Bundle bundle                     = new Bundle(1);

        bundle.putInt(LESSON_FLAG_KEY, lesson);
        speakingFragment.setArguments(bundle);

        return speakingFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mediator_ = (StartUpActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        lesson_ = getArguments().getInt(LESSON_FLAG_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LessonPage page       = new LessonPage(inflater, this, lesson_);

        page.setOneClickExercise(1, "oneClickEx");
//        page.setOneClickExercise(3, "oneClickEx");
        page.setOneClickExercise(4, "compilerEx");

//        page.setOneClickExercise(2, "compilerEx");

        return page.getMainView();
    }
}
