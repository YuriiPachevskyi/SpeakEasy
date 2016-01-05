package com.example.yurii.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.yurii.adapter.TabPagerItem;
import com.example.yurii.adapter.ViewPagerAdapter;
import com.example.yurii.speakeasy.ContentFragment;
import com.example.yurii.speakeasy.R;
import com.example.yurii.speakeasy.StartUpMediator;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends Fragment {
    public static String TAG = "ViewPagerFragment";
    private List<TabPagerItem> mTabs = new ArrayList<>();
    private int lesson_;
    private static String LESSON_FLAG_KEY    = "LessonFlagKey";

    public static ViewPagerFragment newInstance(int lesson) {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle bundle = new Bundle(1);

        bundle.putInt(LESSON_FLAG_KEY, lesson);
        viewPagerFragment.setArguments(bundle);

        return viewPagerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lesson_ = getArguments().getInt(LESSON_FLAG_KEY);
        Log.e(TAG, "onCreate lesson = " + lesson_);
        createTabPagerItem();
    }

    private void createTabPagerItem() {
        Log.e(TAG, "createTabPagerItem");
        mTabs.add(new TabPagerItem(getString(R.string.lesson), ContentFragment.newInstance(lesson_)));
        mTabs.add(new TabPagerItem(getString(R.string.homework), MainFragment.newInstance(getString(R.string.homework))));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_viewpager, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mViewPager.setOffscreenPageLimit(mTabs.size());
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), mTabs));
        TabLayout mSlidingTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSlidingTabLayout.setElevation(15);
        }
        mSlidingTabLayout.setupWithViewPager(mViewPager);
    }
}
