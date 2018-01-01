package com.example.android.worldquiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    public static final String FRAGMENT_KEY_TEMPLATE = "fragmentNumber%d";
    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private ViewPager mViewPager;
    Queue<Fragment> questionFragments = new ArrayDeque<>();
    String[] questions;
    String[] answers;
    SectionsStatePagerAdapter adapter;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questions = getResources().getStringArray(R.array.questions);
        answers = getResources().getStringArray(R.array.answers);
        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        setupViewPager(mViewPager);
        if (savedInstanceState != null){
            manager = getSupportFragmentManager();
            List<Fragment> fragments = manager.getFragments();
            int numOfFragments = fragments.size();
            for (int i=0; i<numOfFragments; i++){
                adapter.addFragment(fragments.get(i));
                questionFragments.remove();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        int numOfFragments = adapter.getCount();
        manager = getSupportFragmentManager();
        for (int i=1; i<numOfFragments; i++){
            manager.putFragment(outState, String.format(FRAGMENT_KEY_TEMPLATE, i) , adapter.getItem(i));
        }
    }

    private void setupViewPager (ViewPager viewPager){
        adapter.addFragment(new WelcomeFragment());
        questionFragments.add(QuestionFragment.newInstance(questions[0], 1, 0));
        questionFragments.add(QuestionFragment.newInstance(questions[1], 2, 0));
        questionFragments.add(MultipleAnswerQuestionFragment.newInstance(questions[2], 3, R.array.options1));
        questionFragments.add(MultipleChoiseQuestionFragment.newInstance(questions[3], 4, R.array.options2));
        questionFragments.add(new EndingFragment());
        viewPager.setAdapter(adapter);
    }

    public void addNextPage() {
        adapter.addFragment(questionFragments.remove());
    }



    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }

    public int getViewPager(){
        return mViewPager.getCurrentItem();
    }

    public int getPagesNumber() {
        return mViewPager.getAdapter().getCount();
    }
}
