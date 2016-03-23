package com.example.yurii.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yurii.adapter.TabPagerItem;
import com.example.yurii.adapter.ViewPagerAdapter;
import com.example.yurii.speakeasy.R;
import com.example.yurii.speakeasy.StartUpActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends Fragment {
    public static String TAG                 = "ViewPagerFragment";
    private static String LESSON_FLAG_KEY    = "LessonFlagKey";
    private List<TabPagerItem> mTabs = new ArrayList<>();
    StartUpActivity mediator_;
    private int     lesson_;

    public static ViewPagerFragment newInstance(int lesson) {
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        Bundle bundle = new Bundle(1);

        bundle.putInt(LESSON_FLAG_KEY, lesson);
        viewPagerFragment.setArguments(bundle);

        return viewPagerFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mediator_ = (StartUpActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lesson_ = getArguments().getInt(LESSON_FLAG_KEY);
        createTabPagerItem();
    }

    private void createTabPagerItem() {
        mTabs.add(new TabPagerItem(getString(R.string.lesson), LessonFragment.newInstance(lesson_)));
        mTabs.add(new TabPagerItem(getString(R.string.speaking), SpeakingFragment.newInstance(lesson_)));
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
        final TabLayout mSlidingTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSlidingTabLayout.setElevation(15);
        }
        mSlidingTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mediator_.updateActionBarColor(position);
                if (position == 0) {
                    mSlidingTabLayout.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundGreenColor)));
                } else if (position == 1) {
                    mSlidingTabLayout.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.backgroundBlueColor)));
                }
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
    }
}
