package com.example.yurii.speakeasy;


import android.app.Activity;
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
    private int lesson_;
    StartUpActivity mediator_;
    private PAGE_TYPE pageType_;
    private LayoutInflater mainInflater_;
    private String currentTag_;
    private ScrollView mainView_;

    private static String LESSON_FLAG_KEY    = "LessonFlagKey";
    private static String PAGE_TYPE_FLAG_KEY = "PageTypeFlagKey";
    private static String TAG_FLAG_KEY       = "TagFlagKey";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        lesson_     = getArguments().getInt(LESSON_FLAG_KEY);
        currentTag_ = getArguments().getString(TAG_FLAG_KEY);
        pageType_   = PAGE_TYPE.fromInteger((int) getArguments().getInt(PAGE_TYPE_FLAG_KEY));
    }

    public static ContentFragment newInstance(PAGE_TYPE type) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle(1);

        bundle.putInt(PAGE_TYPE_FLAG_KEY, type.getValue());
        contentFragment.setArguments(bundle);

        return contentFragment;
    }

    public static ContentFragment newInstance(int lesson) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle(2);

        bundle.putInt(LESSON_FLAG_KEY, lesson);
        bundle.putInt(PAGE_TYPE_FLAG_KEY, PAGE_TYPE.LESSON.getValue());
        contentFragment.setArguments(bundle);

        return contentFragment;
    }

    public static ContentFragment newInstance(String tag) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle(2);

        bundle.putInt(PAGE_TYPE_FLAG_KEY, PAGE_TYPE.TAG.getValue());
        bundle.putString(TAG_FLAG_KEY, tag);
        contentFragment.setArguments(bundle);

        return contentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PageConstructor constructor = null;
        mainInflater_ = inflater;

        try {
            DataBaseCopyist copyist = new DataBaseCopyist(container.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ( pageType_ == PAGE_TYPE.TAG ) {
            constructor = new MaterialConstructor(this);
        } else if ( pageType_ == PAGE_TYPE.LESSON ) {
            constructor = new LessonConstructor(this);
        } else if ( pageType_ == PAGE_TYPE.LESSONS ) {
            constructor = new LessonsConstructor(this);
        } else if ( pageType_ == PAGE_TYPE.TAGS ) {
            constructor = new TagsConstructor(this);
        }
        constructor.constructPage();

        return constructor.getMainView();
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

    public class PageConstructor {
        protected com.example.yurii.speakeasy.CommonPage page_;
        protected DBMaterial     material_;


        public PageConstructor(ContentFragment fragment) {
            page_ = new com.example.yurii.speakeasy.CommonPage(mainInflater_, fragment);
        }

        public void constructPage() {
        }

        public View getMainView() {
            return page_.getMainView();
        }

    }

    public class LessonConstructor extends PageConstructor {
        protected DBLessonConfig lessonConfig_;

        public LessonConstructor(ContentFragment fragment) {
            super(fragment);
            material_     = new DBMaterial(getContext(), lesson_);
            lessonConfig_ = new DBLessonConfig(getContext(), lesson_);
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
                        page_.setVocabulary(new DBVocabulary(getContext()).getVocabularyByLesson(lesson_));
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
            material_ = new DBMaterial(getContext(), lesson_);
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
            DBLessonConfig config = new DBLessonConfig(getContext(), 0);
            page_.setOnclickLessons(config.getLessonsList());
        }
    }

    public class TagsConstructor extends PageConstructor {

        public TagsConstructor(ContentFragment fragment) {
            super(fragment);
            material_ = new DBMaterial(getContext(), lesson_);
        }

        public void constructPage() {
            page_.setOnclickTags(material_.getFullTagsList());
        }
    }
}
