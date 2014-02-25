package com.protege.fizzbuzz;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity 
{
	ListView listView;
	ArrayList<String> fizzbuzzList;
	FizzArrayAdapter fizzAdapter;
	SearchView searchView;
	SharedPreferences sharedPrefs;
	private static final int MAX_NUMBER = 1000;
	private static final String WHATSNEW_PREF = "com.example.fizzbuzzlistview.whatsnew";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPrefs = getSharedPreferences(WHATSNEW_PREF, 0);

		checkVersion();
		
		/**
		 * Using a custom list item isn't necessary in this case, but I just wanted to demonstrate that I am familiar 
		 * with creating custom views within a list. android.R.layout.simple_list_item_1 would be sufficient in this case.
		 */
		listView = (ListView)findViewById(R.id.fizzListView);
		fizzbuzzList = new ArrayList<String>();
		fizzAdapter = new FizzArrayAdapter(this, R.layout.fizz_list_item, fizzbuzzList);
		listView.setAdapter(fizzAdapter);

		for(int i = 0; i<MAX_NUMBER; i++){
			fizzbuzzList.add(fizzBuzz(i));
		}

		fizzAdapter.notifyDataSetChanged();

		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Toast.makeText(parent.getContext(), fizzAdapter.getItem(position), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		final MenuItem menuItem = menu.findItem(R.id.Action_search);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menuItem.getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setQueryHint(getResources().getString(R.string.search_hint));
		searchView.setSubmitButtonEnabled(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			//TODO Add a few settings (ie, setting number of max items in the list)
			return true;
		}
		return false;
	}

	@SuppressLint("NewApi")
	@Override
	public void onNewIntent(Intent intent) 
	{
		super.onNewIntent(intent);      

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			int scrollToPosition = getIntFromString(query);

			if(scrollToPosition >=0 && scrollToPosition < MAX_NUMBER){
				listView.setSelection(getIntFromString(query));
			}else if(scrollToPosition >= MAX_NUMBER){
				Toast.makeText(this, getResources().getString(R.string.large_number), Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, getResources().getString(R.string.bad_number), Toast.LENGTH_SHORT).show();
			}

			if(android.os.Build.VERSION.SDK_INT >= 14){
				searchView.onActionViewCollapsed();
			}

			searchView.clearFocus();
			dismissKeyboard();
		}
	}

	private void dismissKeyboard()
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
	}

	private int getIntFromString(String userString)
	{
		try { 
			return Integer.parseInt(userString); 
		} catch(NumberFormatException e) { 
			return -1; 
		}
	}

	private String fizzBuzz(int i)
	{
		//Checks should always go from most specific to least specific
		if(i % 5 == 0 && i % 3 == 0){
			return i + " fizzbuzz";
		}else if(i %  5 == 0){
			return i + " buzz";
		}else if(i % 3 == 0){
			return i + " fizz";
		}else{
			return i + "";
		}
	}
	
	/**
	 * If the version has been changed, display a dialog detailing the new changes
	 */
	public void checkVersion(){
		String currentVersion = getCurrentVersion(this);
		if(!sharedPrefs.getString(WHATSNEW_PREF, "0").equals(currentVersion)){
			WhatsNewDialog whatsNewFrag = new WhatsNewDialog();
			whatsNewFrag.show(getSupportFragmentManager(), "dialog");

			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putString(WHATSNEW_PREF, currentVersion);
			editor.commit();		
		}
	}
	
	private String getCurrentVersion(Context context){
		PackageInfo pInfo = null;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class FizzArrayAdapter extends ArrayAdapter<String>
	{
		Context context;
		int resource;

		public FizzArrayAdapter(Context context, int resource,
				ArrayList<String> fizzList) 
		{
			super(context, resource, fizzList);
			this.context = context;
			this.resource = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final ViewHolder viewHolder;
			final String fizzText = getItem(position);

			//Only need to inflate the layout if we are not recycling the same view. ViewHolder holds the view in question
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(resource, parent, false);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.getTextView().setText(fizzText);

			return convertView;
		}
	}

	/**
	 * Used to store the reusable view within a tag
	 * @author juliancurrie
	 */
	public static class ViewHolder
	{
		TextView textView;
		View view;

		public ViewHolder(View v){
			view = v;
		}

		//Keeps us from having to call findViewById over and over again.
		public TextView getTextView(){
			return (TextView) view.findViewById(R.id.fizzText);
		}
	}
}
