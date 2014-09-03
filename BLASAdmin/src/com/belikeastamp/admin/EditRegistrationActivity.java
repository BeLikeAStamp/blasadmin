package com.belikeastamp.admin;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.belikeastamp.admin.model.Inscription;
import com.belikeastamp.admin.model.Workshop;
import com.belikeastamp.admin.util.HourPickerDialogFragment;
import com.belikeastamp.admin.util.InscriptionController;
import com.belikeastamp.admin.util.WorkshopController;

public class EditRegistrationActivity extends Activity {
	private static final int SEND_EMAIL = 0;
	private TextView name, number, email, expertise, participants, date, see_wk;
	private Button invit, complet, valider;
	private Workshop w;
	private Inscription ins;
	private String messageType = "";
	private String selectedHour = "00:00";
	private int pHour;
	private int pMinute;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editinscription);

		w = (Workshop) getIntent().getSerializableExtra("workshop");
		ins = (Inscription) getIntent().getSerializableExtra("inscription");

		Log.d("Workshop","=>"+w.toString());
		Log.d("Inscription","=>"+ins.toString());

		name = (TextView) findViewById(R.id.name);
		number = (TextView) findViewById(R.id.number);
		email = (TextView) findViewById(R.id.email);
		expertise = (TextView) findViewById(R.id.expertise);
		date = (TextView) findViewById(R.id.date);
		participants = (TextView) findViewById(R.id.participant);
		see_wk = (TextView) findViewById(R.id.see_wk);

		setInscriptionData();

		invit = (Button) findViewById(R.id.invit);
		complet = (Button) findViewById(R.id.complet);
		valider = (Button) findViewById(R.id.valider);	

		see_wk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditRegistrationActivity.this);
				alertDialogBuilder.setTitle(w.getTheme());
				alertDialogBuilder
				.setMessage(w.toStringSexy())
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
					}

				}); 

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});



		invit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				messageType = getResources().getString(R.string.email_body_invit, w.getPrice());
				sendEmail(messageType);
				updateInscriptionStatus("en attente");
			}
		});

		complet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				messageType = getResources().getString(R.string.email_body_complet);
				sendEmail(messageType);
				updateInscriptionStatus("rejeté : complet");
			}
		});

		valider.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new HourPickerDialogFragment(new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						
						final Calendar c = Calendar.getInstance();
						c.set(Calendar.HOUR_OF_DAY, hourOfDay);
						c.set(Calendar.MINUTE, minute);
						selectedHour = hourOfDay+":"+minute;
						messageType = getResources().getString(R.string.email_body_validate, w.getDate(), selectedHour, w.getHostname(), w.getAddress(), w.getTown());
						sendEmail(messageType);
					}

				});

				newFragment.show(getFragmentManager(), "hourPicker");		
				updateWorkshop();
				updateInscriptionStatus("validé");
			}
		});


	}


	private void sendEmail(String message) {
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,  new String[]{ins.getEmail()});

		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject_inscription, w.getTheme()));
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

		startActivityForResult(emailIntent, SEND_EMAIL);
	}



	private void setInscriptionData() {
		name.setText(getResources().getString(R.string.workshop)+" : "+w.getTheme());
		number.setText(getResources().getString(R.string.phone)+" : "+ins.getPhoneNumber());
		email.setText(getResources().getString(R.string.email)+" : "+ins.getEmail());
		expertise.setText(getResources().getString(R.string.level)+" : "+ins.getExpertise());
		date.setText(getResources().getString(R.string.inscription_date)+" : "+ins.getInscriptionDate());
		participants.setText(getResources().getString(R.string.nbr_participant)+" : "+(ins.getPartcipants()+1));
	}


	private void updateWorkshop() {
		
		final Thread update = new Thread() {
			public void run() {
				WorkshopController c = new WorkshopController();
				try {

					Workshop ws = w;
					int registered = w.getRegistered() + ins.getPartcipants() + 1;
					ws.setRegistered(registered);
					c.update(ws);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		update.start();
	}


	private void updateInscriptionStatus(final String status) {
		
		final Thread update = new Thread() {
			public void run() {
				InscriptionController c = new InscriptionController();
				try {

					Inscription inscription = ins;
					inscription.setInscriptionStatus(status);
					c.update(inscription);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		update.start();
		
	}


	
}
