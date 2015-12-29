package com.example.yurii.speakeasy;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.yurii.database.DBLessonConfig;
import com.example.yurii.database.DBMaterial;
import com.example.yurii.database.DBVocabulary;
import com.example.yurii.database.DataBaseCopyist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.yurii.speakeasy.StartUpMediator.PAGE_TYPE;

public class ContentFragment extends Fragment {

    private static String TAG = "ContentFragment";
    StartUpActivity mediator_;
    private Context context_;
    private PAGE_TYPE pageType_;
    private LayoutInflater mainInflater_;
    private PageConstructor constructor_;
    private String currentTag_;
    private int lesson_;
    private ScrollView mainView_;

    public ContentFragment(Context context, PAGE_TYPE pageType, int lesson) {
        this.setRetainInstance(true);
        lesson_ = lesson;
        context_ = context;
        pageType_ = pageType;
        constructor_ = null;
    }

    public ContentFragment(Context context, PAGE_TYPE pageType, String tag) {
        this(context, pageType, 0);
        currentTag_ = tag;
    }

    public Context getFragmentContext() {
        return context_;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainInflater_ = inflater;

        try {
            DataBaseCopyist copyist = new DataBaseCopyist(container.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ( pageType_ == PAGE_TYPE.TAG ) {
            constructor_ = new MaterialConstructor(this);
        } else if ( pageType_ == PAGE_TYPE.LESSON ) {
            constructor_ = new LessonConstructor(this);
        } else if ( pageType_ == PAGE_TYPE.LESSONS ) {
            constructor_ = new LessonsConstructor(this);
        } else if ( pageType_ == PAGE_TYPE.TAGS ) {
            constructor_ = new TagsConstructor(this);
        }
        mainView_ = (ScrollView) inflater.inflate(R.layout.common_scroll_view, null);
        constructor_.setMainView(mainView_);
        constructor_.constructPage();

        return constructor_.getMainView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mediator_ = (StartUpActivity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static ContentFragment newInstance(Context context, PAGE_TYPE type, int lesson) {
        return new ContentFragment(context, type, lesson);
    }

    public static ContentFragment newInstance(Context context, PAGE_TYPE type, String tag) {
        return new ContentFragment(context, type, tag);
    }

    public class PageConstructor {
        protected com.example.yurii.speakeasy.CommonPage page_;
        protected DBMaterial     material_;


        public PageConstructor(ContentFragment fragment) {
            page_ = new com.example.yurii.speakeasy.CommonPage(mainInflater_, fragment);
        }

        public void constructPage() {
        }

        public void setMainView(ScrollView mainView) {
            page_.setMainView(mainView);
        }

        public View getMainView() {
            return page_.getMainView();
        }

    }

    public class LessonConstructor extends PageConstructor {
        protected DBLessonConfig lessonConfig_;

        public LessonConstructor(ContentFragment fragment) {
            super(fragment);
            material_     = new DBMaterial(context_, lesson_);
            lessonConfig_ = new DBLessonConfig(context_, lesson_);
        }

        public void constructPage() {
            List<String> blocksStructure = lessonConfig_.getKeyValuePageConfig();

            for (String subblock: blocksStructure) {
                List<String> keyValueNode = new ArrayList(Arrays.asList(subblock.split("\\:")));
                String key    = keyValueNode.get(0);
                int value = Integer.valueOf(keyValueNode.get(1));
                switch (key){
                    case "tags": {
                        page_.setTags(material_);
                        break;
                    }
                    case "vocabulary": {
                        page_.setVocabulary(new DBVocabulary(context_).getVocabularyByLesson(lesson_));
                        break;
                    }
                    case "tag": {
                        String tag = material_.getTagsList().get(value-1);
                        page_.setMaterial(tag, material_);
                        break;
                    }
                    case "sEx" : {
                        page_.setSimpleExercise(value, lesson_);
                        break;
                    }
                    case "pEx" : {
                        page_.setColumnDividedImageView(value, lesson_);
                        break;
                    }
                    case "pEx1" : {
                        page_.setPictureExercise1(value, lesson_);
                        break;
                    }
                    case "pEx2" : {
                        page_.setPictureExercise2(value, lesson_);
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        }
    }

    public class MaterialConstructor extends PageConstructor {

        public MaterialConstructor(ContentFragment fragment) {
            super(fragment);
            material_ = new DBMaterial(context_, lesson_);
            Log.e(TAG, "result = " + material_.getFullTagsList());
        }

        public void constructPage() {
            page_.setMaterial(currentTag_, material_);
        }
    }

    public class LessonsConstructor extends PageConstructor {

        public LessonsConstructor(ContentFragment fragment) {
            super(fragment);
        }

        public void constructPage() {
            DBLessonConfig config = new DBLessonConfig(context_, 0);
            page_.setOnclickLessons(config.getLessonsList());
        }
    }

    public class TagsConstructor extends PageConstructor {

        public TagsConstructor(ContentFragment fragment) {
            super(fragment);
            material_ = new DBMaterial(context_, lesson_);
        }

        public void constructPage() {
            page_.setOnclickTags(material_.getFullTagsList());
        }
    }
}
