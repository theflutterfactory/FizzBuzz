package com.protege.fizzbuzz;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity 
{
	SharedPreferences sharedPrefs;
	private static final String WHATSNEW_PREF = "com.protege.fizzbuzzlistview.whatsnew";
	FizzBuzzPageAdapter fizzBuzzAdapter;  
	ViewPager viewPager;
	ActionBar aBar;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setSubtitle("Welcome!");
		
		sharedPrefs = getSharedPreferences(WHATSNEW_PREF, 0);
		checkVersion();

		fizzBuzzAdapter = new FizzBuzzPageAdapter(getSupportFragmentManager());  

		viewPager = (ViewPager) findViewById(R.id.pager);  
		viewPager.setAdapter(fizzBuzzAdapter);  
	}

	@Override
	public void onNewIntent(Intent intent) 
	{
		super.onNewIntent(intent);   

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			SearchFragment fragment = (SearchFragment)getFragmentByPosition(1);
			fragment.handleUserSearch(intent);
		}
	}

	/**
	 * return the fragment based on the default android tag name convention
	 * @param position fragment position
	 * @return the fragment if found
	 */
	private Fragment getFragmentByPosition(int position) 
	{
		String tag = "android:switcher:" + viewPager.getId() + ":" + position;
		return getSupportFragmentManager().findFragmentByTag(tag);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
		case R.id.action_settings:
			//TODO Add a few settings (ie, setting number of max items in the list)
			return true;
		}
		return false;
	}

	/**
	 * If the version has been changed, display a dialog detailing the new changes
	 */
	public void checkVersion()
	{
		String currentVersion = getCurrentVersion(this);
		if(!sharedPrefs.getString(WHATSNEW_PREF, "0").equals(currentVersion)){
			WhatsNewDialog whatsNewFrag = new WhatsNewDialog();
			whatsNewFrag.show(getSupportFragmentManager(), "dialog");

			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putString(WHATSNEW_PREF, currentVersion);
			editor.commit();		
		}
	}

	private String getCurrentVersion(Context context)
	{
		PackageInfo pInfo = null;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public class FizzBuzzPageAdapter extends FragmentPagerAdapter 
	{
		public FizzBuzzPageAdapter(FragmentManager fm) 
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position) 
		{
			switch(position){
			case 0:
				return new GameFragment();
			case 1:
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
		public CharSequence getPageTitle(int position) 
		{
			switch(position){
			case 0:
				return getResources().getString(R.string.game);
			case 1:
				return getResources().getString(R.string.search);
			}
			return null;
		}
	}
}
