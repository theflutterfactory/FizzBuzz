package com.protege.fizzbuzz;

import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameFragment extends Fragment implements OnClickListener 
{
	FragmentActivity activity;
	TextView randomNumText;
	TextView confirmationText;
	TextView bothText;
	ImageView fizzImage;
	ImageView buzzImage;
	int fizzBuzzValue;

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
		setHasOptionsMenu(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		outState.putString(activity.getResources().getString(R.string.randomnumber), randomNumText.getText().toString());
		outState.putString(activity.getResources().getString(R.string.confirmtext), confirmationText.getText().toString());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View v = inflater.inflate(R.layout.fragment_game, container, false);
		randomNumText = (TextView)v.findViewById(R.id.random_number_text);
		confirmationText = (TextView)v.findViewById(R.id.confirmation_text);
		bothText = (TextView)v.findViewById(R.id.both_text);
		fizzImage = (ImageView)v.findViewById(R.id.fizz_image);
		buzzImage = (ImageView)v.findViewById(R.id.buzz_image);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		randomNumText.setOnClickListener(this);
		confirmationText.setOnClickListener(this);
		fizzImage.setOnClickListener(this);
		buzzImage.setOnClickListener(this);
		bothText.setOnClickListener(this);

		if(savedInstanceState == null){
			setFizzBuzzValue();
		}else{
			randomNumText.setText(savedInstanceState.getString(activity.getResources().getString(R.string.randomnumber)));
			confirmationText.setText(savedInstanceState.getString(activity.getResources().getString(R.string.confirmtext)));
		}
	}

	/**
	 * We only want to generate a number that's divisible by 3 or 5. Choosing which based on whether the current
	 * millisecond date value is even or odd. No need to account for being divisible by both.
	 * @return a fizz buzz value thats divisible by 3, 5, or both
	 */
	private int setFizzBuzzValue()
	{
		try{
			Date date = new Date();
			if(date.getTime() % 2 == 0){
				fizzBuzzValue = calculateSpecificRandomNumber(FizzBuzzHelper.DIVISIBLE_BY_3);
			}else{
				fizzBuzzValue = calculateSpecificRandomNumber(FizzBuzzHelper.DIVISIBLE_BY_5);
			}
			randomNumText.setText(fizzBuzzValue + "");
		}catch(Exception e){
			Toast.makeText(activity, activity.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Diving by the multiple ensures that we remain in the 0-999 range
	 * @param multiple
	 * @return a random int from 0-999
	 * @throws ArithmeticException
	 */
	private static int calculateSpecificRandomNumber(int multiple)
	{
		Random random = new Random();
		if(multiple > 0){
			return multiple * random.nextInt(FizzBuzzHelper.MAX_FIZZBUZZ_NUMBER / multiple) ;
		}else{
			throw new ArithmeticException();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		inflater.inflate(R.menu.main, menu);
		menu.removeItem(R.id.Action_search);
	}

	@Override
	public void onClick(View v) 
	{
		switch(v.getId()){
		case R.id.random_number_text:
			break;
		case R.id.confirmation_text:
			break;
		case R.id.fizz_image:
			setConfirmationText(FizzBuzzHelper.FIZZ);
			break;
		case R.id.buzz_image:
			setConfirmationText(FizzBuzzHelper.BUZZ);
			break;
		case R.id.both_text:
			setConfirmationText(FizzBuzzHelper.FIZZBUZZ);
			break;
		}
	}

	private void setConfirmationText(int expectedResult)
	{
		try{
			if(FizzBuzzHelper.calculateFizzBuzz(Integer.parseInt((String) randomNumText.getText())) == expectedResult){
				confirmationText.setText(activity.getResources().getString(R.string.correct));
			}else{
				confirmationText.setText(activity.getResources().getString(R.string.wrong));
			}
			setFizzBuzzValue();
		}catch(Exception e){
			Toast.makeText(activity, activity.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}