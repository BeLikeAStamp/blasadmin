package com.belikeastamp.admin.util;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class HourPickerDialogFragment extends DialogFragment {

	private TimePickerDialog.OnTimeSetListener mTimeSetListener;
	// variables to save user selected date and time
	public  int hour,minute;  
	// declare  the variables to Show/Set the date and time when Time and  Date Picker Dialog first appears
	private int mHour,mMinute; 

	public HourPickerDialogFragment() {}
	
	public HourPickerDialogFragment(OnTimeSetListener listener) {
		mTimeSetListener = listener;
	}

	@Override  
	public Dialog onCreateDialog(Bundle savedInstanceState) {  
		// Use the current date as the default date in the picker  
		final Calendar c = Calendar.getInstance();  
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		TimePickerDialog tpd = new TimePickerDialog(getActivity(), mTimeSetListener, mHour, mMinute, true);
		return tpd;
	}  
}
