package com.belikeastamp.admin.util;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DatePickerDialogFragment extends DialogFragment {

	private DatePickerDialog.OnDateSetListener mDateSetListener;

	public DatePickerDialogFragment() {  

	}  

	public DatePickerDialogFragment(OnDateSetListener dateSetListener) {
		mDateSetListener = dateSetListener;  
	}  

	@Override  
	public Dialog onCreateDialog(Bundle savedInstanceState) {  
		// Use the current date as the default date in the picker  
		final Calendar c = Calendar.getInstance();  
		int year = c.get(Calendar.YEAR);  
		int month = c.get(Calendar.MONTH);  
		int day = c.get(Calendar.DAY_OF_MONTH);  

		// Create a new instance of DatePickerDialog and return it  
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
		dpd.getDatePicker().setCalendarViewShown(false);  
		dpd.getDatePicker().setSpinnersShown(true);  



		return dpd;  
	}  
}
