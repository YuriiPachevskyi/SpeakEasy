package com.example.yurii.page;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.TextView;

import com.example.yurii.database.DBHomework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.example.yurii.page.Exercise.EXERCISE_TYPE.COMPILATION;
import static com.example.yurii.page.Exercise.EXERCISE_TYPE.ONE_CLICK;

public class Exercise {
    private static String TAG = "Exercise";
    public static enum EXERCISE_TYPE { ONE_CLICK, COMPILATION }
    private int           number_;
    private int           subSection_;
    private int           mistakesQuantity_;
    private TextView      targetTextView_;
    private TextView      resultTextView_;
    private DBHomework    exercise_;
    private EXERCISE_TYPE exerciseType_;
    Map<String, String> keyAnswer = new HashMap<String, String>();
    String[] messagesArray = {"Epic winn!", "Almost winn!", "Not so bad!", "Please, repeat exercise again!"};

    Exercise(int number, DBHomework exercise, EXERCISE_TYPE type) {
        number_           = number;
        exercise_         = exercise;
        exerciseType_     = type;
        subSection_       = 0;
        mistakesQuantity_ = 0;
        fillKeyAnswerList();
    }

    public void fillKeyAnswerList() {
        for (int i = 0; i < getContentList().size(); i++ ) {
            keyAnswer.put(getContentList().get(i), getAnswerList().get(i));
        }
    }

    public void updateResultTextView(String word) {
        Pattern cpecialChars  = Pattern.compile("[,.?]");
        Pattern upperCaseChar = Pattern.compile("[A-Z]");


        if ( exerciseType_ == ONE_CLICK) {
            String resultText = targetTextView_.getText().toString().replaceAll("[_]", word);

            resultText = resultText.substring(3, resultText.length());
            resultTextView_.setText(resultText);

        } else if ( exerciseType_ == COMPILATION ) {
            String answer = getAnswerAsList().get(getResultTextViewAsListSize());

            if ( cpecialChars.matcher(answer).find() ) {
                String tempWord = word + answer.substring(answer.length() - 1, answer.length());
                if ( tempWord.equals(answer.toLowerCase()) ) {
                    word = tempWord;
                }
            }
            if ( upperCaseChar.matcher(answer).find() ) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
            }
            if ( getAnswerAsList().size() > getResultTextViewAsList().size() + 1 ) {
                resultTextView_.setText(getResultText() + word + " ");
            } else {
                resultTextView_.setText(getResultText() + word);
            }
        }
    }

    public void handleResultTextViewOnclickEvent() {
        if (resultTextView_.getCurrentTextColor() == targetTextView_.getCurrentTextColor()) {
            List<String> resultText = new ArrayList(Arrays.asList(getResultText().split(" ")));

            String lastWord = resultText.get(resultText.size() - 1) + " ";
            resultTextView_.setText(getResultText().replaceAll(lastWord, ""));
        } else if ( resultTextView_.getText().equals(messagesArray[mistakesQuantity_]) ) {
            subSection_ = 0;
            mistakesQuantity_ = 0;
            resultTextView_.setText(new String());
            targetTextView_.setText(getContentList().get(subSection_));
            resultTextView_.setTextColor(targetTextView_.getCurrentTextColor());
            fillKeyAnswerList();
        } else {
            subSection_ += 1;
            if ( subSection_ < getContentList().size() ) {
                targetTextView_.setText(getContentList().get(subSection_));
                resultTextView_.setText(new String());
                resultTextView_.setTextColor(targetTextView_.getCurrentTextColor());
            } else if ( !keyAnswer.isEmpty() ) {
                List<String> list = new ArrayList<String>(keyAnswer.keySet());

                targetTextView_.setText(list.get(list.size() - 1));
                resultTextView_.setText(new String());
                resultTextView_.setTextColor(targetTextView_.getCurrentTextColor());
            } else {
                resultTextView_.setText(messagesArray[mistakesQuantity_]);
            }
        }
    }

    public void handleTargetTextViewOnclickEvent(String word) {
        if ( keyAnswer.containsKey(getTargetText()) ) {
            if ( resultTextView_.getCurrentTextColor() == targetTextView_.getCurrentTextColor() ) {
                updateResultTextView(word);
            }
            if ( getAnswerAsList().size() == getResultTextViewAsList().size() ) {
                if ( keyAnswer.get(getTargetText()).equals(getResultText()) ) {
                    resultTextView_.setTextColor(Color.parseColor("#31B404"));
                    keyAnswer.remove(getTargetText());
                } else {
                    resultTextView_.setTextColor(Color.parseColor("#FE2E2E"));
                    if ( subSection_ < getContentList().size() ) {
                        mistakesQuantity_ = ++mistakesQuantity_ < 3 ? mistakesQuantity_ : 3;
                    }
                }
            }
        }
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

    public EXERCISE_TYPE getExerciseType() {
        return exerciseType_;
    }

    public String getTargetText() {
        return targetTextView_.getText().toString();
    }

    public String getResultText() {
        return resultTextView_.getText().toString();
    }

    public String getAnswer() {
        return keyAnswer.get(getTargetText());
    }

    public List<String> getContentList() {
        return new ArrayList(Arrays.asList(exercise_.getContent(number_).split("\\|")));
    }

    public List<String> getAnswerList() {
        return new ArrayList(Arrays.asList(exercise_.getAnswer(number_).split("\\|")));
    }

    public List<String> getVariantsList() {
        if ( exerciseType_ == COMPILATION ) {
            List<String> variantsList = getAnswerList();
            String str                = new String();
            Set<String> hs            = new HashSet<>();

            for (String block: variantsList) {
                block = block.replaceAll("[.,?]","").toLowerCase();
                str += block + " ";
            }
            variantsList =  new ArrayList(Arrays.asList(str.split(" ")));
            hs.addAll(variantsList);
            variantsList.clear();
            variantsList.addAll(hs);

            return variantsList;
        }

        return new ArrayList(Arrays.asList(exercise_.getVariants(number_).split("\\|")));
    }

    public List<String> getResultTextViewAsList() {
        return new ArrayList(Arrays.asList(getResultText().split(" ")));
    }

    public List<String> getAnswerAsList() {
        return new ArrayList(Arrays.asList(getAnswer().split(" ")));
    }

    public int getResultTextViewAsListSize( ) {
        return resultTextView_.getText().toString().equals("") ? 0 : getResultTextViewAsList().size();
    }
}
