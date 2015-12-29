package com.example.yurii.speakeasy;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import br.liveo.model.HelpLiveo;
import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.navigationliveo.NavigationLiveo;

public class StartUpActivity extends NavigationLiveo implements OnItemClickListener, StartUpMediator {
    private static String TAG               = "StartUpActivity";
    private static String LESSONS_FRAGMENT  = "LessonsFragment";
    private static String LESSON_FRAGMENT   = "LessonFragment";
    private static String TAGS_FRAGMENT     = "TagsFragment";
    private static String TAG_FRAGMENT      = "TagFragment";

    private ContentFragment lessonsFragment;
    private ContentFragment lessonFragment;
    private ContentFragment tagsFragment;
    private ContentFragment tagFragment;

    private HelpLiveo mHelpLiveo;
    private PAGE_TYPE currentWindowState;
    private PAGE_TYPE prevWindowState;
    private int       currentLesson;

    public StartUpActivity() {
        currentLesson   = 0;
        currentWindowState = PAGE_TYPE.LESSONS;
        prevWindowState    = PAGE_TYPE.LESSONS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lessonsFragment = (ContentFragment)getSupportFragmentManager()
                .findFragmentByTag(LESSONS_FRAGMENT);
        lessonFragment = (ContentFragment)getSupportFragmentManager()
                .findFragmentByTag(LESSON_FRAGMENT);
        tagsFragment    = (ContentFragment) getSupportFragmentManager()
                .findFragmentByTag(TAGS_FRAGMENT);
        tagFragment    = (ContentFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if ( savedInstanceState == null ) {
            lessonsFragment = ContentFragment.newInstance(getApplicationContext(), PAGE_TYPE.LESSONS, "");
            tagsFragment    = ContentFragment.newInstance(getApplicationContext(), PAGE_TYPE.TAGS, "");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.lessons_frame_layout, lessonsFragment, LESSONS_FRAGMENT)
                    .replace(R.id.tags_frame_layout, tagsFragment, TAGS_FRAGMENT)
                    .commit();
        }
        updateWindowState();
    }

    @Override
    public void onBackPressed(){
        if ( currentWindowState == PAGE_TYPE.LESSONS ) {
            this.finish();
        } else if ( prevWindowState == PAGE_TYPE.TAGS ) {
            showTagsList();
        } else if ( prevWindowState == PAGE_TYPE.LESSONS || prevWindowState == PAGE_TYPE.TAG ) {
            showLessonsList();
        } else if ( prevWindowState == PAGE_TYPE.LESSON ) {
            showLesson(currentLesson);
        }
    }

    @Override
    public void onInt(Bundle savedInstanceState) {
        this.userBackground.setImageResource(R.drawable.ic_user_background_first);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.lessons), R.mipmap.ic_star_black_24dp);
        mHelpLiveo.add(getString(R.string.speaking), R.mipmap.ic_inbox_black_24dp);
        mHelpLiveo.addSubHeader(getString(R.string.categories));
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
                break;
            case 2:
                break;
            case 3:
                showTagsList();
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
        updateWindowState();
    }

    public void showSpeakingList() {
        Log.i(TAG, "showSpeakingList");
        prevWindowState = currentWindowState;
        updateWindowState();
    }

    public void showTagsList() {
        Log.i(TAG, "showTagsList");
        prevWindowState = currentWindowState;
        currentWindowState = PAGE_TYPE.TAGS;
        updateWindowState();
    }

    @Override
    public void showMaterial(String tag) {
        Log.i(TAG, "showMaterial");
        prevWindowState    = currentWindowState;
        currentWindowState = PAGE_TYPE.TAG;
        tagFragment     = ContentFragment.newInstance(getApplicationContext(), currentWindowState, tag);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.tag_frame_layout, tagFragment, TAG_FRAGMENT)
                .commit();
        updateWindowState();
    }

    @Override
    public void showLesson(int lesson) {
        Log.i(TAG, "showLesson");
        if ( currentLesson != lesson ) {
            lessonFragment  = ContentFragment.newInstance(getApplicationContext(), PAGE_TYPE.LESSON, lesson);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.lesson_frame_layout, lessonFragment, LESSON_FRAGMENT)
                    .commit();
        }
        currentLesson      = lesson;
        prevWindowState    = currentWindowState;
        currentWindowState = PAGE_TYPE.LESSON;
        updateWindowState();
    }

    public void updateWindowState() {
        getWindow().findViewById(R.id.lessons_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.LESSONS ? View.VISIBLE : View.GONE);
        getWindow().findViewById(R.id.lesson_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.LESSON ? View.VISIBLE : View.GONE);
        getWindow().findViewById(R.id.tags_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.TAGS ? View.VISIBLE : View.GONE);
        getWindow().findViewById(R.id.tag_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.TAG ? View.VISIBLE : View.GONE);
    }
}
