package com.example.yurii.page;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import com.example.yurii.speakeasy.R;

public class LessonPage {
    private static String TAG = "LessonPage";
    private ViewGroup.LayoutParams layoutParams;
    private Fragment parentFragment_;
    private ScrollView mainView_;
    private TableLayout mainTableLayout;
    private Context context;

    private int            displayWidth;
    private int            minColumnWidth;
    private int            maxColumnWidth;
    private int            displayHeight;


    public LessonPage(LayoutInflater inflater, Fragment fragment) {
        Point size      = new Point();
        parentFragment_ = fragment;
        mainTableLayout = (TableLayout) new TableLayout(fragment.getContext());
        layoutParams    = new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        mainView_       = (ScrollView) inflater.inflate(R.layout.common_scroll_view, null);
//        parentFragment_.getActivity().getWindowManager().getDefaultDisplay().getSize(size);
//        displayWidth    = size.x;
//        displayHeight   = size.y;
//        minColumnWidth  = displayWidth < displayHeight ? displayWidth/2 : displayHeight/2;
//        maxColumnWidth  = displayWidth < displayHeight ? displayHeight/2 : displayWidth/2;
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

    public View getMainView() {
        mainView_.addView(mainTableLayout);
        return mainView_;
    }
}
