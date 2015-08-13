package com.steg;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.steg.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Reclamation extends Activity {
	public static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/Reclamation.php";
	EditText NumTel, Email;
	Spinner Panne;
	Button Envoyer;

	String NumeroTel, EMail, panne;

	String[] Pannes = { "copure du courant pondant la nuit",
			"panne dans la capteur", "index n est pas clair" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reclamation);
		NumTel = (EditText) findViewById(R.id.editText1);
		Email = (EditText) findViewById(R.id.editText2);
		Panne = (Spinner) findViewById(R.id.spinner1);
		Envoyer = (Button) findViewById(R.id.button1);

		ArrayAdapter<String> adapter_panne = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, Pannes);
		adapter_panne
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Panne.setAdapter(adapter_panne);

		Envoyer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (NumTel.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Reclamation.this);
					adb.setTitle("Info");
					adb.setMessage("Remplir Numero Tel");
					adb.setPositiveButton("Ok", null);
					adb.show();
					NumTel.requestFocus();
				} else if (NumTel.getText().toString().length() != 8) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Reclamation.this);
					adb.setTitle("Info");
					adb.setMessage("Numero Tel doit etre de 8 chiffre");
					adb.setPositiveButton("Ok", null);
					adb.show();
					NumTel.requestFocus();
				} else if (Email.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Reclamation.this);
					adb.setTitle("Info");
					adb.setMessage("Remplir E-mail");
					adb.setPositiveButton("Ok", null);
					adb.show();
					Email.requestFocus();
				} else {
					NumeroTel = NumTel.getText().toString();
					EMail = Email.getText().toString();
					panne = Panne.getSelectedItem().toString();
					new AjoutReclamation().execute();
				}

			}
		});
	}

	private class AjoutReclamation extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(Reclamation.this, "Progress",
					"Envoie de reclamation en cour..", false, false);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			JsonParser js = new JsonParser(strURL, NumeroTel, EMail, panne,
					null, null, null, null, null, null, null);
			js.initialisation(NumeroTel, EMail, panne, null, null, null, null,
					null, null, null);

			try {
				js.Connect(strURL);
				return true;
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						Reclamation.this);
				adb.setTitle("Info");
				adb.setMessage("Erreur Réseaux");
				adb.setPositiveButton("Ok", null);
				adb.show();
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();

			if (result) {
				Intent i = new Intent(Reclamation.this,
						ReclamationTerminer.class);
				startActivity(i);
			} else {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						Reclamation.this);
				adb.setTitle("Info");
				adb.setMessage("Erreur Réseaux");
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
