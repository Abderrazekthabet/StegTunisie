package com.steg;


import java.io.InputStream;

import java.math.BigInteger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Edinar extends Activity {
	EditText num, pass;
	Button valider;
	String Num, pwd, result = "";
	public static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/paieEdinar.php";

	JSONArray ar;
	InputStream is;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edinar);
		num = (EditText) findViewById(R.id.editText1);
		pass = (EditText) findViewById(R.id.editText2);
		valider = (Button) findViewById(R.id.button1);

		valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Num = num.getText().toString();	
				pwd = pass.getText().toString();

				/*****************************************/
				try {

					long lg = Long.parseLong(Num);
					BigInteger NM = BigInteger.valueOf(lg);

			
				/***********************************************/
				try {
					int passe = Integer.parseInt(pwd);
				
				/*********************************************/
				if (num.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Edinar.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Numero Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					num.requestFocus();
					return;
				}
				if (pass.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Edinar.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Code Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					pass.requestFocus();
					return;
				}
				if (Num.length() != 16) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Edinar.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Numero Edinar doit etre de 16 chiffre");
					adb.setPositiveButton("Ok", null);
					adb.show();
					num.requestFocus();
					return;
				}
				if (pwd.length() != 4) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Edinar.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Code doit etre de 4 chiffre");
					adb.setPositiveButton("Ok", null);
					adb.show();
					pass.requestFocus();
					return;
				}
			
				
				new VerifEdinar().execute();

				} catch (Exception e) {

				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						Edinar.this);
				adb1.setTitle("Vérifier");
				adb1.setMessage("Code doit etre Numérique");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
				pass.requestFocus();
			}} catch (Exception e) {

				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						Edinar.this);
				adb1.setTitle("Vérifier");
				adb1.setMessage("Numero Edinar doit etre Numérique");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
				num.requestFocus();
			}}
		});

	}

	private class VerifEdinar extends AsyncTask<Void, Void, JSONArray> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(Edinar.this, "Progress",
					"Verification en cour..", false, false);

		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JsonParser js = new JsonParser(strURL, Num, pwd, null, null, null,
					null, null, null, null, null);
			js.initialisation(Num, pwd, null, null, null, null, null, null,
					null, null);

			try {
				is = js.Connect(strURL);
			} catch (Exception e) {
				AlertDialog.Builder adb1 = new AlertDialog.Builder(Edinar.this);
				adb1.setTitle("Vérification");
				adb1.setMessage("Erreur Réseaux");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
			}
			try {
				result = js.ConvertToString(is);
			} catch (Exception e) {
				AlertDialog.Builder adb1 = new AlertDialog.Builder(Edinar.this);
				adb1.setTitle("Vérification");
				adb1.setMessage("Aucune donnée récupiré");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
			}

			try {
				ar = js.Analyse(result);
			} catch (Exception e) {
				AlertDialog.Builder adb1 = new AlertDialog.Builder(Edinar.this);
				adb1.setTitle("Vérification");
				adb1.setMessage("Aucune donnée récupiré");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
			}

			return ar;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();

			if (result == null) {
				AlertDialog.Builder adb1 = new AlertDialog.Builder(Edinar.this);
				adb1.setTitle("Vérification2");
				adb1.setMessage("Code OU Numero Edinar Incorrecte");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
				pass.requestFocus();
			} else {
				try {
					
					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						String p = json_data.getString("motdepasse");
						String n = json_data.getString("NumeroEdinar");
						String s = json_data.getString("solde");
						if (p.equals(pass.getText().toString())
								&& n.equals(num.getText().toString())) {
							Intent it = new Intent(Edinar.this,
									PaiementFactureEdinar.class);
							it.putExtra("numeroEdinar", n);
							it.putExtra("sol", s);
							startActivity(it);
						} else {
							Toast.makeText(Edinar.this,
									"SVP Vérifier Vos cordonnées",
									Toast.LENGTH_LONG).show();
						}

					}
				} catch (JSONException e) {
					AlertDialog.Builder adb1 = new AlertDialog.Builder(
							Edinar.this);
					adb1.setTitle("Vérification2");
					adb1.setMessage("Code OU Numero Edinar Incorrecte");
					adb1.setPositiveButton("Ok", null);
					adb1.show();
					pass.requestFocus();
				}
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
