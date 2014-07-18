package com.belikeastamp.admin;

import com.belikeastamp.admin.model.Workshop;
import com.belikeastamp.admin.util.WorkshopController;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditWorkshopActivity extends Activity {
	private EditText theme, address, hostname, town, date, cap,reg ;
	private Button edit, save, cancel, del, back;
	final WorkshopController c = new WorkshopController();
	private Workshop w;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editworkshop);

		w = (Workshop) getIntent().getSerializableExtra("workshop");


		theme = (EditText) findViewById(R.id.theme);
		address = (EditText) findViewById(R.id.address);
		hostname = (EditText) findViewById(R.id.hostname);
		town = (EditText) findViewById(R.id.town);
		date = (EditText) findViewById(R.id.date);
		cap = (EditText) findViewById(R.id.capacity);
		reg = (EditText) findViewById(R.id.registered);

		setWorshopData();

		edit = (Button) findViewById(R.id.edit);
		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
		del = (Button) findViewById(R.id.delete);
		back =  (Button) findViewById(R.id.back);

		edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				theme.setEnabled(true);
				address.setEnabled(true);
				hostname.setEnabled(true);
				town.setEnabled(true);
				date.setEnabled(true);
				cap.setEnabled(true);
				reg.setEnabled(true);

				save.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.VISIBLE);
				del.setEnabled(false);
			}
		});

		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(theme.getEditableText().length() > 0 
						&& address.getEditableText().length() > 0 
						&& hostname.getEditableText().length() > 0
						&& town.getEditableText().length() > 0
						&& date.getEditableText().length() > 0
						&& cap.getEditableText().length() > 0
						&& reg.getEditableText().length() > 0) {
					if(Integer.valueOf(cap.getText().toString()) < 1) 
						Toast.makeText(getApplicationContext(), "Capacité inférieure à 1 ???...", Toast.LENGTH_SHORT).show();
					else
					{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditWorkshopActivity.this);
						alertDialogBuilder.setTitle("Mise à jour");
						alertDialogBuilder
						.setMessage("Es-tu sûre que tu veux écraser définitivement cette entrée ?")
						.setCancelable(false)
						.setPositiveButton("Oui je suis sûre", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								updateWorkshop();
							}


						})
						.setNegativeButton("Oups ! non non", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						}); 

						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
				}
				else {
					// TOAST
					Toast.makeText(getApplicationContext(), "Il manque des infos...", Toast.LENGTH_SHORT).show();
				}
			}


		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Modifications annulées ! ", Toast.LENGTH_SHORT).show();

				theme.setEnabled(false);
				address.setEnabled(false);
				hostname.setEnabled(false);
				town.setEnabled(false);
				date.setEnabled(false);
				cap.setEnabled(false);
				reg.setEnabled(false);
				del.setEnabled(true);
				cancel.setVisibility(View.INVISIBLE);
				save.setVisibility(View.INVISIBLE);
				setWorshopData();
			}
		});

		del.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditWorkshopActivity.this);
				alertDialogBuilder.setTitle("Suppression");
				alertDialogBuilder
				.setMessage("Es-tu sûre que tu veux supprimer définitivement cette entrée ?")
				.setCancelable(false)
				.setPositiveButton("Oui je suis sûre", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						deleteWorkshop();
					}


				})
				.setNegativeButton("Oups ! non non", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				}); 

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EditWorkshopActivity.this, GetAllWorkshopActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

	}

	private void setWorshopData() {
		// TODO Auto-generated method stub
		Log.d("EDIT", "Remplissage ");
		theme.setText(w.getTheme());
		address.setText(w.getAddress());
		hostname.setText(w.getHostname());
		town.setText(w.getTown());
		date.setText(w.getDate());
		cap.setText(""+w.getCapacity());
		reg.setText(""+w.getRegistered());
	}

	private void updateWorkshop() {
		// TODO Auto-generated method stub
		final Thread update = new Thread() {
			public void run() {
				Log.d("EDIT", "MAJ ");
				try {
					Workshop ws = new Workshop();
					ws.setId(w.getId());
					ws.setAddress(address.getText().toString());
					ws.setCapacity(Integer.valueOf(cap.getText().toString()));
					ws.setRegistered(Integer.valueOf(reg.getText().toString()));
					ws.setDate(date.getText().toString());
					ws.setHostname(hostname.getText().toString());
					ws.setTheme(theme.getText().toString());
					ws.setTown(town.getText().toString());
					Log.d("EDIT", town.getText().toString());
					w = ws;
					c.update(ws);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		update.start();
	}


	private void deleteWorkshop() {
		// TODO Auto-generated method stub
		final Thread delete = new Thread() {
			public void run() {
				Log.d("EDIT", "Suppression ");
				try {
					c.delete(w.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		delete.start();
	}
}
