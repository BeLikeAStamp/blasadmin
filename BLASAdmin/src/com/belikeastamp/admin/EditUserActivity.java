package com.belikeastamp.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.belikeastamp.admin.model.User;
import com.belikeastamp.admin.util.UserController;

public class EditUserActivity extends Activity {
	private EditText firstname, address, name, email, phone;
	private CheckBox partener, host ;
	private Button edit, save, cancel, del, back;
	final UserController c = new UserController();
	private User u;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edituser);

		u = (User) getIntent().getSerializableExtra("user");

		firstname = (EditText) findViewById(R.id.firstname);
		address = (EditText) findViewById(R.id.address);
		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);
		partener = (CheckBox) findViewById(R.id.partener);
		host = (CheckBox) findViewById(R.id.host);

		setUserData();

		edit = (Button) findViewById(R.id.edit);
		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
		del = (Button) findViewById(R.id.delete);
		back =  (Button) findViewById(R.id.back);


		edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				firstname.setEnabled(true);
				address.setEnabled(true);
				name.setEnabled(true);
				email.setEnabled(true);
				phone.setEnabled(true);
				host.setEnabled(true);
				partener.setEnabled(true);

				save.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.VISIBLE);
				del.setEnabled(false);
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Modifications annulées ! ", Toast.LENGTH_SHORT).show();

				firstname.setEnabled(false);
				address.setEnabled(false);
				name.setEnabled(false);
				email.setEnabled(false);
				phone.setEnabled(false);
				host.setEnabled(false);
				partener.setEnabled(false);
				del.setEnabled(true);
				cancel.setVisibility(View.INVISIBLE);
				save.setVisibility(View.INVISIBLE);
				setUserData();
			}
		});

		del.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditUserActivity.this);
				alertDialogBuilder.setTitle("Suppression");
				alertDialogBuilder
				.setMessage("Es-tu sûre que tu veux supprimer définitivement cette entrée ?")
				.setCancelable(false)
				.setPositiveButton("Oui je suis sûre", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						deleteUser();
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
				Intent i = new Intent(EditUserActivity.this, GetAllUserActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});


		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditUserActivity.this);
				alertDialogBuilder.setTitle("Mise à jour");
				alertDialogBuilder
				.setMessage("Es-tu sûre que tu veux écraser définitivement cette entrée ?")
				.setCancelable(false)
				.setPositiveButton("Oui je suis sûre", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						updateUser();
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
	}

	private void setUserData() {
		// TODO Auto-generated method stub
		Log.d("EDIT", "Remplissage ");
		firstname.setText(u.getFirstname());
		address.setText(u.getAddress());
		name.setText(u.getName());
		email.setText(u.getEmail());
		phone.setText(u.getPhone());
		host.setChecked(u.getIsHost());
		partener.setChecked(u.getIsPartener());
	}

	private void deleteUser() {
		final Thread delete = new Thread() {
			public void run() {
				Log.d("EDIT", "Suppression ");
				try {
					c.delete(u.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final Intent intent = new Intent(EditUserActivity.this,
						GetAllUserActivity.class);
				startActivity(intent);
			}
		};

		delete.start();
	}

	private void updateUser() {
		// TODO Auto-generated method stub
		final Thread update = new Thread() {
			public void run() {
				Log.d("EDIT", "MAJ ");
				try {
					User user = new User();
					user.setId(u.getId());
					user.setAddress(address.getText().toString());
					user.setEmail(email.getText().toString());
					user.setFirstname(firstname.getText().toString());
					user.setName(name.getText().toString());
					user.setPhone(phone.getText().toString());
					user.setIsHost(host.isChecked());
					user.setIsPartener(partener.isChecked());
					u = user;
					c.update(user);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final Intent intent = new Intent(EditUserActivity.this,
						GetAllUserActivity.class);
				startActivity(intent);
			}
		};

		update.start();
	}

}
