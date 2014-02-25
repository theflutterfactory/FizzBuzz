package com.protege.fizzbuzz;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.widget.TextView;

public class WhatsNewDialog extends DialogFragment 
{

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{		
		final Dialog newDialog = new Dialog(getActivity());				
		newDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		newDialog.getWindow().setWindowAnimations(android.R.anim.fade_in);
		newDialog.setContentView(R.layout.whatsnew);
		newDialog.show();

		TextView newText = (TextView)newDialog.findViewById(R.id.whatsnew_text);

		StringBuilder sBuilder = new StringBuilder();

		String[] newArray = getResources().getStringArray(R.array.whats_new);

		for(String line : newArray){
			sBuilder.append("- " + line + System.getProperty("line.separator") + System.getProperty("line.separator"));
		}

		newText.setText(sBuilder.toString());

		return newDialog;		
	}
}
