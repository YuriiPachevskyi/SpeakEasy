package com.example.yurii.page;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.yurii.database.DBHomework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ExerciseConfig {
    private int           number_;
    private int           subSection_;
    private int           mistakeRandIndex_;
    private TextView      targetTextView_;
    private TextView      resultTextView_;
    private DBHomework    exercise_;
    private List<Integer> mistakeList_;
    private String        exerciseType_;

    ExerciseConfig(LessonPage parentPage, int number, DBHomework exercise, String exersiceType) {
        number_           = number;
        exercise_         = exercise;
        exerciseType_     = exersiceType;
        subSection_       = 0;
        mistakeRandIndex_ = 0;
        mistakeList_      = new ArrayList<Integer>();
    }

    public void resetSection() {
        subSection_ = 0;
    }

    public void resetResultTextViewColor() {
        resultTextView_.setTextColor(targetTextView_.getCurrentTextColor());
    }

    public void incSection() {
        subSection_ += 1;
    }

    public int getSection() {
        if ( subSection_ >= getContentListSize() ) {
            return getSectionFromMistakeList();
        }
        return subSection_;
    }

    public int getSectionFromMistakeList() {
        if ( !mistakeList_.isEmpty() ) {
            Random rand = new Random();

            mistakeRandIndex_ = rand.nextInt(mistakeList_.size());
            return mistakeList_.get(mistakeRandIndex_);
        }

        return -1;
    }

    public TextView setTargetView(TextView view) {
        targetTextView_ = view;
        targetTextView_.setTextSize(18);

        return view;
    }

    public TextView setResultView(TextView view) {
        resultTextView_ = view;
        resultTextView_.setBackground(new ColorDrawable(Color.parseColor("#FAEBD7")));
        resultTextView_.setTextSize(18);

        return view;
    }

    public void setResultTextViewColor(String color) {
        resultTextView_.setTextColor(Color.parseColor(color));
    }

    public TextView getTargetView() {
        return targetTextView_;
    }

    public TextView getResultView() {
        return resultTextView_;
    }

    public String getExerciseType() {
        return exerciseType_;
    }

    public String getTargetText() {
        return targetTextView_.getText().toString();
    }

    public String getResultText() {
        return resultTextView_.getText().toString();
    }

    public String getAnswer() {
        if ( subSection_ < getContentListSize() ) {
            return getAnswerList().get(getSection());
        }
        return getAnswerList().get(mistakeList_.get(mistakeRandIndex_));
    }

    public int getNumber() {
        return number_;
    }

    public int getContentListSize() {
        return getContentList().size();
    }

    public int getAnswerListSize() {
        return getAnswerList().size();
    }

    public List<String> getContentList() {
        return new ArrayList(Arrays.asList(exercise_.getContent(number_).split("\\|")));
    }

    public List<String> getAnswerList() {
        return new ArrayList(Arrays.asList(exercise_.getAnswer(number_).split("\\|")));
    }

    public List<String> getVariantsList() {
        return new ArrayList(Arrays.asList(exercise_.getVariants(number_).split("\\|")));
    }

    public List<String> getVariantsList(List<String> list) {
        String str = new String();
        Set<String> hs = new HashSet<>();
        List<String> result = null;

        for (String block: list) {
            block = block.replaceAll("[.,?]","").toLowerCase();
            str += block + " ";
        }
        result =  new ArrayList(Arrays.asList(str.split(" ")));
        hs.addAll(result);
        result.clear();
        result.addAll(hs);

        return result;
    }

    public void handleMistakeList(String str) {
        if ( str.equals(getResultText()) ) {
            setResultTextViewColor("#31B404");
            if ( subSection_ >= getContentListSize() ) {
                mistakeList_.remove(mistakeRandIndex_);
            }
        } else {
            Set<Integer> hs = new HashSet<Integer>();

            setResultTextViewColor("#FE2E2E");
            mistakeList_.add(getSection());
            hs.addAll(mistakeList_);
            mistakeList_.clear();
            mistakeList_.addAll(hs);
        }
    }
}
