package com.example.yurii.page;


import com.example.yurii.database.DBHomework;
import com.example.yurii.speakeasy.CommonPage;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class LessonPage extends CommonPage {
    private static String TAG = "LessonPage";

    DBHomework           exerciseDB_;
    List<Exercise> exercises_;

    public LessonPage(LayoutInflater inflater, Fragment fragment, int lesson) {
        super(inflater, fragment);

        exerciseDB_      = new DBHomework(parentFragment_.getContext(), lesson);
        exercises_       = new ArrayList<Exercise>();
    }

    public void setOneClickExercise(int section, Exercise.EXERCISE_TYPE type) {
        final Exercise exercise   = new Exercise(section, exerciseDB_, type);
        final TextView resultTextView = getSimpleTextView(new String());
        final TextView targetTextView = getSimpleTextView(exercise.getContentList().get(0));

        setSignature(exerciseDB_.getSignature(section), -1);
        if ( !exerciseDB_.getExamples(section).isEmpty() ) {
            setInstruction("Example:");
            trimConfigValuesAndSetTextView(exerciseDB_.getExamples(section));
        }
        mainTableLayout.addView(exercise.setTargetView(targetTextView));
        mainTableLayout.addView(exercise.setResultView(resultTextView));
        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                exercise.handleResultTextViewOnclickEvent();
            }
        });
        setVariants(exercise.getVariantsList(), exercise);
        exercises_.add(exercise);
    }

    public void setVariants(List<String> list, final Exercise exercise) {
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
                        exercise.handleVariantOnclickEvent(((TextView) viewIn).getText().toString());
                    }
                });
                llv.addView(textView);
            }
            mainTableLayout.addView(llv);
        }
    }
}
