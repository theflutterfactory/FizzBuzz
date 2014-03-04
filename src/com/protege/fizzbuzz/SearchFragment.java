package com.protege.fizzbuzz;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class SearchFragment extends Fragment 
{
	ListView listView;
	ArrayList<String> fizzbuzzList;
	FizzArrayAdapter fizzAdapter;
	SearchView searchView;
	FragmentActivity activity;

	@Override
	public void onAttach(Activity parentActivity)
	{
		super.onAttach(parentActivity);
		activity = (FragmentActivity) parentActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_search, container, false);
		listView = (ListView)v.findViewById(R.id.fizzListView);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);

		fizzbuzzList = new ArrayList<String>();
		fizzAdapter = new FizzArrayAdapter(activity, R.layout.fizz_list_item, fizzbuzzList);
		listView.setAdapter(fizzAdapter);

		for(int i = 0; i<FizzBuzzHelper.MAX_FIZZBUZZ_NUMBER; i++){
			fizzbuzzList.add(printFizzBuzzValue(i, FizzBuzzHelper.calculateFizzBuzz(i)));
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		inflater.inflate(R.menu.main, menu);
		final MenuItem menuItem = menu.findItem(R.id.Action_search);

		SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menuItem.getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
		searchView.setQueryHint(getResources().getString(R.string.search_hint));
		searchView.setSubmitButtonEnabled(true);
	}

	@SuppressLint("NewApi")
	public void handleUserSearch(Intent intent)
	{
		String query = intent.getStringExtra(SearchManager.QUERY);
		int scrollToPosition = getScrollToPositionFromString(query);

		if(scrollToPosition >= 0 && scrollToPosition < FizzBuzzHelper.MAX_FIZZBUZZ_NUMBER){
			listView.setSelection(getScrollToPositionFromString(query));
		}else if(scrollToPosition >= FizzBuzzHelper.MAX_FIZZBUZZ_NUMBER){
			Toast.makeText(activity, getResources().getString(R.string.large_number), Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(activity, getResources().getString(R.string.bad_number), Toast.LENGTH_SHORT).show();
		}

		if(android.os.Build.VERSION.SDK_INT >= 14){
			searchView.onActionViewCollapsed();
		}

		searchView.clearFocus();
		dismissKeyboard();
	}

	private void dismissKeyboard()
	{
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
	}

	private int getScrollToPositionFromString(String userString)
	{
		try { 
			return Integer.parseInt(userString); 
		} catch(NumberFormatException e) { 
			return -1; 
		}
	}

	private String printFizzBuzzValue(int position, int FizzBuzzValue)
	{
		switch(FizzBuzzValue){
		case FizzBuzzHelper.FIZZ:
			return position + " fizz";
		case FizzBuzzHelper.BUZZ:
			return position + " buzz";
		case FizzBuzzHelper.FIZZBUZZ:
			return position + " fizzbuzz";
		default: 
			return position + "";
		}
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

			//Only need to inflate the layout if we are not recycling the same view
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

	public static class ViewHolder
	{
		TextView textView;
		View view;

		public ViewHolder(View v)
		{
			view = v;
		}

		public TextView getTextView()
		{
			return (TextView) view.findViewById(R.id.fizzText);
		}
	}
}
