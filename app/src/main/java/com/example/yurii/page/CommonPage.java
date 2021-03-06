package com.example.yurii.speakeasy;


import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.yurii.database.DBExercise;
import com.example.yurii.database.DBMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonPage {
    private static String TAG = "CommonPage";
    protected ViewGroup.LayoutParams layoutParams;
    protected Fragment parentFragment_;
    protected TableLayout mainTableLayout;
    private ScrollView mainView_;

    protected int          displayWidth;
    private int            minColumnWidth;
    private int            maxColumnWidth;
    private int            displayHeight;


    public CommonPage(LayoutInflater inflater, Fragment fragment) {
        Point size      = new Point();
        parentFragment_ = fragment;
        mainTableLayout = (TableLayout) new TableLayout(fragment.getContext());
        layoutParams    = new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        mainView_       = (ScrollView) inflater.inflate(R.layout.common_scroll_view, null);
        parentFragment_.getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        displayWidth    = size.x;
        displayHeight   = size.y;
        minColumnWidth  = displayWidth < displayHeight ? displayWidth/2 : displayHeight/2;
        maxColumnWidth  = displayWidth < displayHeight ? displayHeight/2 : displayWidth/2;
    }

    public TextView getSimpleTextView(String str) {
        TextView textView = new TextView(parentFragment_.getContext());

        textView.setText(str);
        textView.setTextSize(16);
        textView.setPadding(12, 0, 12, 12);
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(Color.parseColor("#484848"));

        return textView;
    }

    public void setTags(DBMaterial materialDB, final StartUpActivity mediator) {
        List<String> tags = materialDB.getTagsList();

        for ( int i = 0, symbolsCounter = 0; i < tags.size(); ) {
            LinearLayout llv = new LinearLayout(parentFragment_.getContext());

            for ( ; i < tags.size(); i++ ) {
                String currentTag = tags.get(i).toString();
                TextView textView = getSimpleTextView(currentTag);

                symbolsCounter += currentTag.length();
                if ( symbolsCounter > 70) {
                    break;
                }
                textView.setPadding(1, 9, 9, 1);
                textView.setGravity(Gravity.CENTER);

                if ( !materialDB.getContentList(currentTag).isEmpty() ) {
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View viewIn) {
                            mediator.showMaterial(((TextView)viewIn).getText().toString());
                        }
                    });
                } else {
                    textView.setTextColor(Color.parseColor("#787878"));
                }
                llv.addView(textView);
            }
            symbolsCounter = 0;
            mainTableLayout.addView(llv);
        }
    }

    public void setVocabulary(List<String> contentList) {
        TextView textView = new TextView(parentFragment_.getContext());

        textView.setText("Vocabulary");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.GRAY);
        textView.setTypeface(null, Typeface.BOLD);
        mainTableLayout.addView(textView);
        setColumnDividedTextView(minColumnWidth, contentList);
    }

    public void setSpeakingInfo(DBExercise exercise, int section) {
        String content  = exercise.getContent(section);
        TextView textView = new TextView(parentFragment_.getContext());

        textView.setText(exercise.getSignature(section));
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(parentFragment_.getResources().getColor(R.color.backgroundBlueColor));
        textView.setTypeface(null, Typeface.BOLD);
        mainTableLayout.addView(textView);
    }

    public void setSimpleExercise(DBExercise exercise, int section) {
        String examples = exercise.getExamples(section);
        String content  = exercise.getContent(section);

        setSignature(exercise.getSignature(section), -1);
        if ( !examples.isEmpty() ) {
            setInstruction("Example:");
            trimConfigValuesAndSetTextView(examples);
        }
        if ( !content.isEmpty() ) {
            trimConfigValuesAndSetTextView(content);
        }
    }

    public void setPictureExercise1(DBExercise exercise, int section) {
        LinearLayout llv = new LinearLayout(parentFragment_.getContext());
        String examples = exercise.getExamples(section);
        String content  = exercise.getContent(section);

        if ( section != 0 ) {
            setSignature(exercise.getSignature(section), -1);
        }
        if ( !content.isEmpty() ) {
            String text = new String();
            List<String> contentList = new ArrayList(Arrays.asList(content.split("\\|")));
            for ( int i = 0; i < contentList.size(); i++ ) {
                text = text.concat(contentList.get(i).toString() + "\n");
            }
            llv.addView(getSimpleTextView(text));
        }
        if ( !examples.isEmpty() ) {
            int resID = parentFragment_.getResources().getIdentifier(examples, "drawable",
                    parentFragment_.getContext().getPackageName());
            ImageView image = new ImageView(parentFragment_.getContext());

            image.setImageResource(resID);
            image.setLayoutParams(displayWidth < displayHeight ?
                    new LinearLayout.LayoutParams(displayWidth/3, displayWidth/3) :
                    new LinearLayout.LayoutParams(displayWidth/3, displayHeight/3));
            image.setPadding(5, 5, 5, 5);
            llv.addView(image);
        }
        mainTableLayout.addView(llv);
    }

    public void setPictureExercise2(DBExercise exercise, int section) {
        LinearLayout llv  = new LinearLayout(parentFragment_.getContext());
        String examples   = exercise.getExamples(section);
        String content    = exercise.getContent(section);
        String subContent = exercise.getSubContent(section);

        if ( section != 0 ) {
            setSignature(exercise.getSignature(section), -1);
        }
        if ( !examples.isEmpty() ) {
            int resID = parentFragment_.getResources().getIdentifier(examples, "drawable",
                    parentFragment_.getContext().getPackageName());
            ImageView image = new ImageView(parentFragment_.getContext());

            image.setImageResource(resID);
            image.setLayoutParams(new LinearLayout.LayoutParams(displayWidth/3, displayHeight/3));
            image.setPadding(5, 5, 5, 5);
            llv.addView(image);
        }
        if ( !content.isEmpty() ) {
            llv.addView(getSimpleTextView(content));
        }
        mainTableLayout.addView(llv);
        if ( !subContent.isEmpty() ) {
            trimConfigValuesAndSetTextView(subContent);
        }
    }

    public void setSignature(final String str, int position) {
        TextView textView = getSimpleTextView(str);

        if ( position == 0 ) {
            textView.setBackgroundColor(parentFragment_.getResources()
                    .getColor(R.color.backgroundGreenColor));
        } else  if ( position == 1 ) {
            textView.setBackgroundColor(parentFragment_.getResources()
                    .getColor(R.color.backgroundBlueColor));
        } else  if ( position == 2 ) {
            textView.setBackgroundColor(parentFragment_.getResources()
                    .getColor(R.color.backgroundYellowColor));
        }
        textView.setPadding(6, 0, 6, 0);
        textView.setTypeface(null, Typeface.BOLD);
        mainTableLayout.addView(textView);
    }

    public void setInstruction(String str) {
        TextView textView = getSimpleTextView(str);

        textView.setTypeface(null, Typeface.BOLD);
        mainTableLayout.addView(textView);
    }

    public void setMaterial(String tag, DBMaterial materialDB, int position) {
        List<String> contentList = materialDB.getContentList(tag);

        setSignature(tag, position);
        for ( String subContent: contentList ) {
            if ( !subContent.isEmpty() ) {
                trimConfigValuesAndSetTextView(subContent);
            }
        }
        String instruction        = materialDB.getInstructionByTag(tag);
        String insturctionContent = materialDB.getInstructionContentByTag(tag);
        if ( !instruction.isEmpty() ) {
            setInstruction(instruction);
        }
        if ( !insturctionContent.isEmpty() ) {
            trimConfigValuesAndSetTextView(insturctionContent);
        }
    }

    public void trimConfigValuesAndSetTextView(String content) {
        int minColumnQuantity = 1;
        int maxColumnQuantity = 1;

        try {
            int last = content.length() - 1;

            minColumnQuantity = Integer.valueOf(content.substring(0, 1));
            maxColumnQuantity = Integer.valueOf(content.substring(last, content.length()));
            content = content.substring(1, last);
        } catch (NumberFormatException exception) {
            Log.e(TAG, "NumberFormatException, error number is not valid content = " + content);
        }

        List<String> list = new ArrayList(Arrays.asList(content.split("\\|")));
        if ( minColumnQuantity == maxColumnQuantity ) {
            setColumnDividedTextView(displayWidth/minColumnQuantity, list);
        } else if ( minColumnQuantity > maxColumnQuantity ) {
            setColumnDividedTextView(minColumnWidth, list);
        } else {
            setColumnDividedTextView(maxColumnWidth, list);
        }
    }

    public void setColumnDividedTextView(int columnWidth, List<String> content) {
        LinearLayout llv = new LinearLayout(parentFragment_.getContext());

        for ( int i = 0; i < (displayWidth / columnWidth); i++ ) {
            String text = new String();
            int elementsQuontity = content.size() / (displayWidth / columnWidth);
            int index = i * elementsQuontity;

            if ( content.size() % elementsQuontity != 0 ) {
                index += i;
                for ( int counter = 0; counter <= elementsQuontity && index < content.size(); counter++, index++ ) {
                    text = text.concat(content.get(index).toString() + "\n");
                }
            } else {
                for ( int counter = 0; counter < elementsQuontity; counter++, index++ ) {
                    text = text.concat(content.get(index).toString() + "\n");
                }
            }
            text = text.substring(0, text.length()-1);
            llv.addView(getSimpleTextView(text));
        }
        mainTableLayout.addView(llv);
    }

    public void setColumnDividedImageView(DBExercise exercise, int section) {
        String examples     = exercise.getExamples(section);
        String content      = exercise.getContent(section);

        if ( !content.isEmpty() ) {
            List<String> signatures_pictures = new ArrayList(Arrays.asList(content.split("\\^")));

            setSignature(exercise.getSignature(section), -1);
            if ( !examples.isEmpty() ) {
                setInstruction("Example:");
                trimConfigValuesAndSetTextView(examples);
            }
            if ( signatures_pictures.size() > 1 ) {
                List<String> picturesList = new ArrayList(Arrays.asList(signatures_pictures.get(1).split("\\|")));

                for ( int i = 0; i < picturesList.size(); ) {
                    LinearLayout llv = new LinearLayout(parentFragment_.getContext());

                    for ( int j = 0; j < 3 && i < picturesList.size(); j++, i++ ) {
                        ImageView imageView = new ImageView(parentFragment_.getContext());
                        int resID = parentFragment_.getResources().getIdentifier(picturesList.get(i),
                                "drawable", parentFragment_.getContext().getPackageName());

                        imageView.setImageResource(resID);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setPadding(6, 0, 6, 6);
                        llv.addView(imageView);
                    }
                    mainTableLayout.addView(llv);
                }
            }
        }
    }

    public View getMainView() {
        mainView_.addView(mainTableLayout);
        return mainView_;
    }
}