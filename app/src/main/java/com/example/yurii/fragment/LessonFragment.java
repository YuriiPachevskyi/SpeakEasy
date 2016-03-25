package com.example.yurii.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yurii.database.DBExercise;
import com.example.yurii.database.DBLesson;
import com.example.yurii.database.DBLessonConfig;
import com.example.yurii.database.DBMaterial;
import com.example.yurii.database.DBVocabulary;
import com.example.yurii.speakeasy.CommonPage;
import com.example.yurii.speakeasy.StartUpActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LessonFragment extends Fragment {
    private static String TAG             = "LessonFragment";
    private static String LESSON_FLAG_KEY = "LessonFlagKey";
    private int           lesson_;
    StartUpActivity       mediator_;

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

    public static LessonFragment newInstance(int lesson) {
        LessonFragment lessonFragment = new LessonFragment();
        Bundle bundle                 = new Bundle(1);

        bundle.putInt(LESSON_FLAG_KEY, lesson);
        lessonFragment.setArguments(bundle);

        return lessonFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonPage     page            = new CommonPage(inflater, this);
        DBMaterial     material        = new DBMaterial(getContext(), lesson_);
        DBExercise     exerciseDB      = new DBLesson(getContext(), lesson_);
        DBLessonConfig lessonConfig    = new DBLessonConfig(getContext(), lesson_);
        List<String>   blocksStructure = lessonConfig.getKeyValueLessonConfig();

        for (String subblock: blocksStructure) {
            List<String> keyValueNode  = new ArrayList(Arrays.asList(subblock.split("\\:")));
            int section                = Integer.valueOf(keyValueNode.get(1));

            switch ( keyValueNode.get(0) ){
                case "tags": {
                    page.setTags(material, mediator_);
                    break;
                }
                case "vocabulary": {
                    page.setVocabulary(new DBVocabulary(getContext()).getVocabularyByLesson(lesson_));
                    break;
                }
                case "tag": {
                    String tag = material.getTagsList().get(section-1);
                    page.setMaterial(tag, material, 0);
                    break;
                }
                case "sEx" : {
                    page.setSimpleExercise(exerciseDB, section);
                    break;
                }
                case "pEx" : {
                    page.setColumnDividedImageView(exerciseDB, section);
                    break;
                }
                case "pEx1" : {
                    page.setPictureExercise1(exerciseDB, section);
                    break;
                }
                case "pEx2" : {
                    page.setPictureExercise2(exerciseDB, section);
                    break;
                }
                default:{
                    break;
                }
            }
        }

        return page.getMainView();
    }
}

