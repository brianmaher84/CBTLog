package com.brianmaher84.cbtlog;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.brianmaher84.cbtlog.db.ActivityDBHelper;
import com.brianmaher84.cbtlog.list.ListActivity;

import java.util.HashMap;
import java.util.Map;

public class CBTLogActivity extends AppCompatActivity {

    public static final String SITUATION = "Situation";
    public static final String FEELINGS = "Feelings";
    public static final String THOUGHTS = "Thoughts";
    public static TabLayout tabLayout;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private ActivityDBHelper activityDBHelper;
    public static Map<String, String> activityDetails = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cbtlog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activityDBHelper = new ActivityDBHelper(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String situation = activityDetails.get(SITUATION);
                final String feelings = activityDetails.get(FEELINGS);
                final String thoughts = activityDetails.get(THOUGHTS);

                if(situation == null || situation == "" || feelings == null || feelings == "" || thoughts == null || thoughts == "") {
                    Snackbar.make(view, "All tabs must have values to save!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                activityDBHelper.insertActivity(situation, feelings, thoughts);
                activityDetails.clear();

                for (int i = 0; i < mViewPager.getChildCount(); i++) {
                    TextView tmpTxtView = (TextView) mViewPager.getChildAt(i).findViewById(R.id.section_label);
                    tmpTxtView.setText("");
                }

                Snackbar.make(view, "Saved!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cbtlog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_HINT_TEXT = "hint_text";
        private static TextView txtView;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String hint_text) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_HINT_TEXT, hint_text);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cbtlog, container, false);
            txtView = (TextView) rootView.findViewById(R.id.section_label);

            final Bundle args = this.getArguments();
            txtView.setHint(args.getString(ARG_HINT_TEXT));
            txtView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    activityDetails.put(getPageTitle(args.getInt(ARG_SECTION_NUMBER)).toString(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            return rootView;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return SITUATION;
                case 1:
                    return FEELINGS;
                case 2:
                    return THOUGHTS;
            }
            return null;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private Map<Integer, Fragment> cache = new HashMap<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (!cache.keySet().contains(position)) {
                cache.put(position, PlaceholderFragment.newInstance(position, getHint(position).toString()));
            }
            return cache.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return SITUATION;
                case 1:
                    return FEELINGS;
                case 2:
                    return THOUGHTS;
            }
            return null;
        }

        public CharSequence getHint(int position) {
            switch (position) {
                case 1:
                    return "What did you feel?\n" +
                            "Rate your emotion 0 -100%";
                case 2:
                    return "What was going through your mind\n" +
                            "as you started to feel this way?\n" +
                            "(Thoughts or images)";
                case 0:
                    return "Who, what, when, where?";
            }
            return null;
        }
    }
}
