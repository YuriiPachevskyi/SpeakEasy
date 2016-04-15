package com.example.yurii.page;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yurii.database.DBHomework;
import com.example.yurii.speakeasy.CommonPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LessonPage extends CommonPage {
    private static String TAG = "LessonPage";

    DBHomework           exercise_;
    List<ExerciseConfig> exercises_;

    public LessonPage(LayoutInflater inflater, Fragment fragment, int lesson) {
        super(inflater, fragment);

        exercise_        = new DBHomework(parentFragment_.getContext(), lesson);
        exercises_       = new ArrayList<ExerciseConfig>();
    }

    public boolean checkIsAll(ExerciseConfig config) {
        String answer = config.getAnswer();
        List<String> answerToList = new ArrayList(Arrays.asList(answer.split(" ")));
        List<String> resultToList = new ArrayList(Arrays.asList(config.getResultText().split(" ")));

        if ( resultToList.size() == answerToList.size() ) {
            if ( config.getExerciseType().equals("compilerEx") ) {
                String lastChar = answer.substring(answer.length() - 1, answer.length());

                updateResultTextView(lastChar, config);
            }

            return true;
        }

        return false;
    }

    void checkIsAnswerCorrect(ExerciseConfig config) {
        String answer = config.getAnswer();
        List<String> answerToList = new ArrayList(Arrays.asList(answer.split(" ")));
        List<String> resultToList = new ArrayList(Arrays.asList(config.getResultText().split(" ")));

        if ( resultToList.size() == answerToList.size() ) {
            if ( config.getExerciseType().equals("compilerEx") ) {
                String lastChar = answer.substring(answer.length() - 1, answer.length());

                updateResultTextView(lastChar, config);
            }

            config.handleMistakeList(answer);
            setNextVariant(config);
        }
    }

    void updateResultTextView(String word, ExerciseConfig config) {
        if ( word.equals("") ) {
            config.getResultView().setText(new String());
            return;
        }
        if ( config.getExerciseType().equals("oneClickEx")) {
            String result = config.getTargetText();
            config.getResultView().setText(result.replaceAll("[_]", word));
            return;
        }

        if ( config.getExerciseType().equals("compilerEx") ) {
            if ( config.getResultText().equals("") ) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
            } else if ( word.equals(".") || word.equals("?") ) {
                String str = (String) config.getResultView().getText();
                config.getResultView().setText(str.substring(0, str.length() - 1) + word);
                return;
            }

            config.getResultView().setText(config.getResultText() + word + " ");
        }
    }

    public void updateTargetTextView(String str, ExerciseConfig config) {
        config.getTargetView().setText(new String());
        config.getTargetView().setText(str);
    }

    public void setNextVariant(ExerciseConfig config) {
        config.incSection();
        int section = config.getSection();

        if ( section >= 0 ) {
            config.getResultView().setTextColor(config.getTargetView().getCurrentTextColor());
            updateTargetTextView(config.getContentList().get(section), config);
            updateResultTextView("", config);
        } else {
            config.getResultView().setText("Epic win!!!");
        }
    }

    public void setOneClickExercise(int section, String type) {
        final TextView resultTextView = getSimpleTextView(new String());
        final TextView targetTextView = getSimpleTextView(new String());
        final ExerciseConfig config   = new ExerciseConfig(this,section, exercise_, type);

        setSignature(exercise_.getSignature(section), -1);
        if ( !exercise_.getExamples(section).isEmpty() ) {
            setInstruction("Example:");
            trimConfigValuesAndSetTextView(exercise_.getExamples(section));
        }
        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                if ( resultTextView.getCurrentTextColor() == targetTextView.getCurrentTextColor() ) {
                    List<String> resultText = new ArrayList(Arrays.asList(config.getResultText().split(" ")));
                    String lastWord = resultText.get(resultText.size() - 1) + " ";
                    config.getResultView().setText(config.getResultText().replaceAll(lastWord, ""));
                } else if ( config.getResultText().equals("Epic win!!!")) {
                    config.resetSection();
                    updateResultTextView("", config);
                    updateTargetTextView(config.getContentList().get(config.getSection()), config);
                } else {
                    setNextVariant(config);
                }
            }
        });
        if ( config.getExerciseType().equals("oneClickEx") ) {
            setVariants(config.getVariantsList(), config);
        } else if ( config.getExerciseType().equals("compilerEx") ) {
            setVariants(config.getVariantsList(config.getAnswerList()), config);
        }
        mainTableLayout.addView(config.setTargetView(targetTextView));
        mainTableLayout.addView(config.setResultView(resultTextView));
        updateTargetTextView(config.getContentList().get(config.getSection()), config);
        exercises_.add(config);
    }

    public void setVariants(List<String> list, final ExerciseConfig config) {
        for ( int i = 0, position = 0; i < list.size(); i++ ) {
            LinearLayout llv = new LinearLayout(parentFragment_.getContext());

            for ( int j = 0; j < 4 && position < list.size(); j++, position++ ) {
                String current = list.get(position).toString();
                TextView textView = getSimpleTextView(current);

                textView.setPadding(2, 12, 2, 12);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(18);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View viewIn) {
                        updateResultTextView(((TextView) viewIn).getText().toString(), config);
                        if ( checkIsAll(config) == true ) {
                            config.handleMistakeList(config.getAnswer());
                        }
                    }
                });
                llv.addView(textView);
            }
            mainTableLayout.addView(llv);
        }
    }
}
