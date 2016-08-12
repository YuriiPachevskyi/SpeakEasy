package com.example.yurii.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.yurii.database.DBLessonConfig;
import com.example.yurii.speakeasy.R;
import com.example.yurii.speakeasy.StartUpActivity;


public class LessonsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static String TAG = "LessonFragment";
    private StartUpActivity mediator_;
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mediator_ = (StartUpActivity) activity;
    }

    public static LessonsFragment newInstance() {
        LessonsFragment lessonFragment = new LessonsFragment();

        return lessonFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DBLessonConfig config = new DBLessonConfig(getContext(), 0);
        View view = inflater.inflate(R.layout.fast_search_frame_layout, container, false);

        mSearchView = (SearchView) view.findViewById(R.id.search_view);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, config.getLessonsList()));
        mListView.setTextFilterEnabled(true);
        setOnItemClickListener();
        setupSearchView();

        return view;
    }

    public void setOnItemClickListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String lessonSignature = (String) arg0.getAdapter().getItem(arg2);
                String lessonNumber = new String();

                for (int i = 0; i < lessonSignature.length(); i++) {
                    if (lessonSignature.charAt(i) == ' ') {
                        break;
                    }
                    lessonNumber += lessonSignature.charAt(i);
                }
                mediator_.showLesson(Integer.parseInt(lessonNumber));
            }
        });
    }

    private void setupSearchView() {
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setQueryHint("Search");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return false;
    }
}
