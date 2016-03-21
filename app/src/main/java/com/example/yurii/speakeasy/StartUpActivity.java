package com.example.yurii.speakeasy;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.yurii.database.DBLessonConfig;
import com.example.yurii.database.DataBaseCopyist;
import com.example.yurii.fragment.LessonsFragment;
import com.example.yurii.fragment.MaterialFragment;
import com.example.yurii.fragment.TagsFragment;
import com.example.yurii.fragment.ViewPagerFragment;

import java.io.IOException;

import br.liveo.model.HelpLiveo;
import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.navigationliveo.NavigationLiveo;

public class StartUpActivity extends NavigationLiveo implements OnItemClickListener, StartUpMediator {
    private static String TAG               = "StartUpActivity";
    private static String LESSONS_FRAGMENT  = "LessonsFragment";
    private static String LESSON_FRAGMENT   = "LessonFragment";
    private static String TAGS_FRAGMENT     = "TagsFragment";
    private static String MATERIAL_FRAGMENT = "MaterialFragment";

    private static String LESSON_FLAG_KEY   = "LessonFlagKey";
    private static String CURRENT_FLAG_KEY  = "CurrentFlagKey";
    private static String PREV_FLAG_KEY     = "PrevFlagKey";

    private LessonsFragment   lessonsFragment;
    private ViewPagerFragment lessonFragment;
    private TagsFragment      tagsFragment;
    private MaterialFragment  materialFragment;

    private View      actionBarNode;
    private HelpLiveo mHelpLiveo;
    private PAGE_TYPE currentWindowState;
    private PAGE_TYPE prevWindowState;
    private int       currentLesson;

    public StartUpActivity() {
        currentLesson      = 0;
        currentWindowState = PAGE_TYPE.LESSONS;
        prevWindowState    = PAGE_TYPE.LESSONS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            DataBaseCopyist copyist = new DataBaseCopyist(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        lessonsFragment  = (LessonsFragment)getSupportFragmentManager()
                .findFragmentByTag(LESSONS_FRAGMENT);
        lessonFragment   = (ViewPagerFragment)getSupportFragmentManager()
                .findFragmentByTag(LESSON_FRAGMENT);
        tagsFragment     = (TagsFragment) getSupportFragmentManager()
                .findFragmentByTag(TAGS_FRAGMENT);
        materialFragment = (MaterialFragment) getSupportFragmentManager()
                .findFragmentByTag(MATERIAL_FRAGMENT);

        actionBarNode = getLayoutInflater().inflate(R.layout.custom_actionbar, null);

        if ( savedInstanceState == null ) {
            lessonsFragment = LessonsFragment.newInstance();
            tagsFragment    = TagsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.lessons_frame_layout, lessonsFragment, LESSONS_FRAGMENT)
                    .replace(R.id.tags_frame_layout, tagsFragment, TAGS_FRAGMENT)
                    .commit();
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(actionBarNode, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        ((Toolbar) actionBarNode.getParent()).setContentInsetsAbsolute(0, 0);
        updateWindowState(true);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(LESSON_FLAG_KEY, currentLesson);
        savedInstanceState.putInt(CURRENT_FLAG_KEY, currentWindowState.getValue());
        savedInstanceState.putInt(PREV_FLAG_KEY, prevWindowState.getValue());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentLesson  = savedInstanceState.getInt(LESSON_FLAG_KEY);
        currentWindowState = PAGE_TYPE.fromInteger((int) savedInstanceState.getInt(CURRENT_FLAG_KEY));
        prevWindowState = PAGE_TYPE.fromInteger((int) savedInstanceState.getInt(PREV_FLAG_KEY));
        updateWindowState(true);
    }

    @Override
    public void onBackPressed(){
        if ( currentWindowState == PAGE_TYPE.LESSONS ) {
            this.finish();
        } else if ( prevWindowState == PAGE_TYPE.TAGS ) {
            showTagsList();
        } else if ( prevWindowState == PAGE_TYPE.LESSONS || prevWindowState == PAGE_TYPE.TAG ) {
            showLessonsList();
        } else if ( prevWindowState == PAGE_TYPE.LESSON && currentWindowState != PAGE_TYPE.TAGS ) {
            showLesson(currentLesson);
        } else if ( currentWindowState == PAGE_TYPE.TAGS ) {
            showLessonsList();
        }
    }

    @Override
    public void onInt(Bundle savedInstanceState) {
        this.userBackground.setImageResource(R.drawable.ic_user_background_first);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.lessons), R.mipmap.ic_star_black_24dp);
        mHelpLiveo.add(getString(R.string.tags), R.mipmap.ic_send_black_24dp);


        with(this, br.liveo.model.Navigation.THEME_LIGHT).startingPosition(0)
                .addAllHelpItem(mHelpLiveo.getHelp())

                .colorItemSelected(R.color.nliveo_blue_colorPrimary)
                .colorNameSubHeader(R.color.nliveo_blue_colorPrimary)
                .footerItem(R.string.settings, R.mipmap.ic_settings_black_24dp)
                .setOnPrepareOptionsMenu(onPrepare)
                .build();

        int position = this.getCurrentPosition();
        this.setElevationToolBar(position != 2 ? 15 : 0);
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case 0:
                showLessonsList();
                break;
            case 1:
                showTagsList();
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
        setElevationToolBar(position != 2 ? 15 : 0);
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    public void showLessonsList() {
        Log.i(TAG, "showLessonsList");
        prevWindowState = currentWindowState;
        currentWindowState = PAGE_TYPE.LESSONS;
        updateWindowState(currentLesson != 0 || prevWindowState != PAGE_TYPE.LESSONS);
    }

    public void showTagsList() {
        Log.i(TAG, "showTagsList");
        prevWindowState = PAGE_TYPE.LESSONS;
        currentWindowState = PAGE_TYPE.TAGS;
        updateWindowState(true);
    }

    @Override
    public void showMaterial(String tag) {
        Log.i(TAG, "showMaterial");
        prevWindowState    = currentWindowState;
        currentWindowState = PAGE_TYPE.TAG;
        materialFragment        = MaterialFragment.newInstance(tag);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.material_frame_layout, materialFragment, MATERIAL_FRAGMENT)
                .commit();
        updateWindowState(false);
    }

    @Override
    public void showLesson(int lesson) {
        if ( currentLesson != lesson ) {
            lessonFragment = ViewPagerFragment.newInstance(lesson);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.lesson_frame_layout, lessonFragment, LESSON_FRAGMENT)
                    .commit();
        }
        currentLesson      = lesson;
        prevWindowState    = currentWindowState;
        currentWindowState = PAGE_TYPE.LESSON;
        updateWindowState(true);
    }

    @Override
    public void updateActionBarColor(int position) {
        if ( position == 0 ) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundGreenColor)));
        } else if ( position == 1 ) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundBlueColor)));
        }
    }

    public void updateWindowState(boolean updateActionBar) {
        getWindow().findViewById(R.id.lessons_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.LESSONS ? View.VISIBLE : View.GONE);
        getWindow().findViewById(R.id.lesson_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.LESSON ? View.VISIBLE : View.GONE);
        getWindow().findViewById(R.id.tags_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.TAGS ? View.VISIBLE : View.GONE);
        getWindow().findViewById(R.id.material_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.TAG ? View.VISIBLE : View.GONE);
        if ( updateActionBar == true ) {
            updateActionBar();
        }
    }

    public void updateActionBar() {
        int orientation = getResources().getConfiguration().orientation;

        getSupportActionBar().show();
        if ( currentWindowState == PAGE_TYPE.LESSONS ) {
            updateActionBarColor(0);
            ((TextView) actionBarNode.findViewById(R.id.lesson_theme)).setText("Lessons");
        } else if ( currentWindowState == PAGE_TYPE.TAGS ) {
            ((TextView) actionBarNode.findViewById(R.id.lesson_theme)).setText("Tags");
        } else if ( currentWindowState == PAGE_TYPE.LESSON ) {
            ((TextView) actionBarNode.findViewById(R.id.lesson_theme)).setText( String.valueOf(currentLesson)
                    + " " + new DBLessonConfig(getApplicationContext(), currentLesson).getSignature());
            if ( orientation == Configuration.ORIENTATION_LANDSCAPE ) {
                getSupportActionBar().hide();
            }
        }
    }
}
