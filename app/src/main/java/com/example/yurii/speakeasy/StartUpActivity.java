package com.example.yurii.speakeasy;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yurii.database.DBLessonConfig;
import com.example.yurii.fragment.ViewPagerFragment;

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

    private static String LESSON_FLAG_KEY   = "LessonFlagKey";
    private static String CURRENT_FLAG_KEY  = "CurrentFlagKey";
    private static String PREV_FLAG_KEY     = "PrevFlagKey";

    private ContentFragment   lessonsFragment;
    private ViewPagerFragment lessonFragment;
    private ContentFragment   tagsFragment;
    private ContentFragment   tagFragment;

    private View actionBarNode;

    private HelpLiveo mHelpLiveo;
    private PAGE_TYPE currentWindowState;
    private PAGE_TYPE prevWindowState;
    private int currentLesson;

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
        lessonFragment = (ViewPagerFragment)getSupportFragmentManager()
                .findFragmentByTag(LESSON_FRAGMENT);
        tagsFragment    = (ContentFragment) getSupportFragmentManager()
                .findFragmentByTag(TAGS_FRAGMENT);
        tagFragment    = (ContentFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        actionBarNode = getLayoutInflater().inflate(R.layout.custom_actionbar, null);

        if ( savedInstanceState == null ) {
            lessonsFragment = ContentFragment.newInstance(PAGE_TYPE.LESSONS, 0, "");
            tagsFragment    = ContentFragment.newInstance(PAGE_TYPE.TAGS, 0, "");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.lessons_frame_layout, lessonsFragment, LESSONS_FRAGMENT)
                    .replace(R.id.tags_frame_layout, tagsFragment, TAGS_FRAGMENT)
                    .commit();
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        updateWindowState(false);
    }

    public void showSpeakingList() {
        Log.i(TAG, "showSpeakingList");
        prevWindowState = currentWindowState;
        updateWindowState(false);
    }

    public void showTagsList() {
        Log.i(TAG, "showTagsList");
        prevWindowState = PAGE_TYPE.LESSONS;
        currentWindowState = PAGE_TYPE.TAGS;
        updateWindowState(false);
    }

    @Override
    public void showMaterial(String tag) {
        Log.i(TAG, "showMaterial");
        prevWindowState    = currentWindowState;
        currentWindowState = PAGE_TYPE.TAG;
        tagFragment     = ContentFragment.newInstance(currentWindowState, 0, tag);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.tag_frame_layout, tagFragment, TAG_FRAGMENT)
                .commit();
        updateWindowState(false);
    }

    @Override
    public void showLesson(int lesson) {
        if ( currentLesson != lesson ) {
            lessonFragment = new ViewPagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.lesson_frame_layout, lessonFragment, LESSON_FRAGMENT)
                    .commit();
        }
        currentLesson      = lesson;
        prevWindowState    = currentWindowState;
        currentWindowState = PAGE_TYPE.LESSON;
        updateWindowState(true);
    }

    public void updateWindowState(boolean updateActionBar) {
        getWindow().findViewById(R.id.lessons_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.LESSONS ? View.VISIBLE : View.GONE);
        getWindow().findViewById(R.id.lesson_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.LESSON ? View.VISIBLE : View.GONE);
        getWindow().findViewById(R.id.tags_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.TAGS ? View.VISIBLE : View.GONE);
        getWindow().findViewById(R.id.tag_frame_layout)
                .setVisibility(currentWindowState == PAGE_TYPE.TAG ? View.VISIBLE : View.GONE);
        if ( updateActionBar == true ) {
            updateActionBar();
        }
    }

    public void updateActionBar() {
        ((TextView) actionBarNode.findViewById(R.id.lesson_number))
                .setText(String.valueOf(currentLesson));
        ((TextView) actionBarNode.findViewById(R.id.lesson_theme))
                .setText(new DBLessonConfig(getApplicationContext(), currentLesson).getSignature());
        if ( currentWindowState == PAGE_TYPE.LESSON ) {
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(actionBarNode, new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            ((Toolbar) actionBarNode.getParent()).setContentInsetsAbsolute(0, 0);
        }
    }
}
