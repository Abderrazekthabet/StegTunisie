package com.steg;



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
import android.widget.Toast;

public class Demmande extends Activity {
	public static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/AjoutDemmande.php";
	EditText NomPrenom, NumTel, Adresse;
	Spinner Ville;
	Button Envoyer;
	String Nom_Prenom, Numero_Tel, adresse,region;
	String[] ville = { "Centre Ville", "Rccada", "Abida", "Nasrallah",
			"Shbika", "Haffouz" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demmande);
		NomPrenom = (EditText) findViewById(R.id.editText1);
		NumTel = (EditText) findViewById(R.id.editText2);
		Adresse = (EditText) findViewById(R.id.editText3);
		Envoyer = (Button) findViewById(R.id.button1);
		Ville = (Spinner) findViewById(R.id.spinner1);

		ArrayAdapter<String> adapter_ville = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, ville);
		adapter_ville
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Ville.setAdapter(adapter_ville);

		Envoyer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (NomPrenom.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Demmande.this);
					adb.setTitle("Info");
					adb.setMessage("Remplir Nom et Prénom");
					adb.setPositiveButton("Ok", null);
					adb.show();
					NomPrenom.requestFocus();
				} else if (NumTel.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Demmande.this);
					adb.setTitle("Info");
					adb.setMessage("Remplir Numero Tel");
					adb.setPositiveButton("Ok", null);
					adb.show();
					NumTel.requestFocus();
				} else if (NumTel.getText().toString().length() != 8) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Demmande.this);
					adb.setTitle("Info");
					adb.setMessage("Numero Tel doit etre de 8 chiffre");
					adb.setPositiveButton("Ok", null);
					adb.show();
					NumTel.requestFocus();
				} else if (Adresse.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Demmande.this);
					adb.setTitle("Info");
					adb.setMessage("Remplir Adresse");
					adb.setPositiveButton("Ok", null);
					adb.show();
					Adresse.requestFocus();
				} else {
					Toast.makeText(
							Demmande.this,
							NomPrenom.getText().toString() + "-"
									+ NumTel.getText().toString() + "-"
									+ Adresse.getText().toString(),
							Toast.LENGTH_LONG).show();
					region =Ville.getSelectedItem().toString();
					Nom_Prenom=NomPrenom.getText().toString();
					Numero_Tel=NumTel.getText().toString(); 
					adresse=Adresse.getText().toString();
					new AjouterDemmande().execute();
				}

			}
		});

	}

	private class AjouterDemmande extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(Demmande.this, "Progress",
					"Ajout en cours..", false, false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			JsonParser js = new JsonParser(strURL, Nom_Prenom, Numero_Tel,
					adresse, region, null, null, null, null, null, null);
			
			js.initialisation( Nom_Prenom, Numero_Tel,
					adresse, region, null, null, null, null, null, null);
			
			try {
				js.Connect(strURL);
				return true;
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						Demmande.this);
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
			if (result==true){
				Intent i=new Intent(Demmande.this,Demmande_Envoyer.class);
				startActivity(i);
			}else {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						Demmande.this);
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
