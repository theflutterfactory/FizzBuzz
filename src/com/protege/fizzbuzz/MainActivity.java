package com.protege.fizzbuzz;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import Util.PreferenceUtil;

public class MainActivity extends FragmentActivity {
    private FizzBuzzPageAdapter fizzBuzzAdapter;
    private ViewPager viewPager;
    private static final int GAME = 0;
    private static final int SEARCH = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setSubtitle(getString(R.string.welcome));
        checkVersion();

        fizzBuzzAdapter = new FizzBuzzPageAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(fizzBuzzAdapter);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            SearchFragment fragment = (SearchFragment)getFragmentByPosition(SEARCH);
            fragment.handleUserSearch(intent);
        }
    }

    private Fragment getFragmentByPosition(int position) {
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public void checkVersion(){
        String currentVersion = getCurrentVersion(this);
        if(!PreferenceUtil.getAppVersion(this).equals(currentVersion)){
            WhatsNewDialog whatsNewFrag = new WhatsNewDialog();
            whatsNewFrag.show(getSupportFragmentManager(), "dialog");
            PreferenceUtil.setAppVersion(this, currentVersion);
        }
    }

    private String getCurrentVersion(Context context){
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class FizzBuzzPageAdapter extends FragmentPagerAdapter {
        public FizzBuzzPageAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case GAME:
                    return new GameFragment();
                case SEARCH:
                    return new SearchFragment();
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case GAME:
                    return getResources().getString(R.string.game);
                case SEARCH:
                    return getResources().getString(R.string.search);
            }
            return null;
        }
    }
}
