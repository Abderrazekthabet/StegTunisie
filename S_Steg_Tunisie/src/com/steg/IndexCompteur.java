package com.steg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IndexCompteur extends Activity {
	private static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/IndexCompteur.php";
	EditText IndexCompteur;
	Button valider;
	int IC;

	InputStream is;
	JSONArray ar;
	String cin, Montant, val, result = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		IndexCompteur = (EditText) findViewById(R.id.editText1);
		valider = (Button) findViewById(R.id.button1);
		/************************************************************/
		// 9belna el montant +validation + cin elli b3athnaHom mel class elli
		// 9ablou elly Houa ConsommationElec.java
		Intent data = this.getIntent();

		Montant = data.getExtras().getString("montant");
		val = data.getExtras().getString("validation");
		cin = data.getExtras().getString("cin");
		/**************************************************************************/
		valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					IC = Integer.parseInt(IndexCompteur.getText().toString());

					new SelectCompteur().execute();

				} catch (Exception e) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							IndexCompteur.this);
					adb.setTitle("Vérification");
					adb.setMessage("Index Compteur doit etre Numérique");
					adb.setPositiveButton("Ok", null);
					adb.show();
				}

			}
		});

	}

	private class SelectCompteur extends AsyncTask<Void, Void, JSONArray> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(IndexCompteur.this,
					"ProgressDialog", "En cours..", false, false);
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JsonParser js = new JsonParser(strURL, cin, null, null, null, null,
					null, null, null, null, null);

			js.initialisation(cin, null, null, null, null, null, null, null,
					null, null);

			try {
				is = js.Connect(strURL);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						IndexCompteur.this);
				adb.setTitle("Info");
				adb.setMessage("Erreur Réseaux");
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
			
			
			result = js.ConvertToString(is);

			try {
				ar = js.Analyse(result);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						IndexCompteur.this);
				adb.setTitle("Vérification1");
				adb.setMessage("Index Compteur Incorrecte");
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
			return ar;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();

			if (result == null) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						IndexCompteur.this);
				adb.setTitle("Vérification2");
				adb.setMessage("Index Compteur Incorrecte");
				adb.setPositiveButton("Ok", null);
				adb.show();
			} else {
				try {

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						String mt = json_data.getString("AncienIndexCompteur");
						int mtt = Integer.parseInt(mt);

						if (IC < mtt) {
							AlertDialog.Builder adb = new AlertDialog.Builder(
									IndexCompteur.this);
							adb.setTitle("Vérification");
							adb.setMessage("Vérifier Votre Index Compteur");
							adb.setPositiveButton("Ok", null);
							adb.show();
							IndexCompteur.requestFocus();
						} else if (IC == mtt) {
							AlertDialog.Builder adb = new AlertDialog.Builder(
									IndexCompteur.this);
							adb.setTitle("Consommation");
							adb.setMessage("Null Consommation");
							adb.setPositiveButton("Ok", null);
							adb.show();
							IndexCompteur.requestFocus();
						} else {
							/***************************************************************/
							// idha kol chay sava ma8 net3addou lel class
							// eli ba3dou Consommation.java elli be8 tsyr
							// fih l'affichage final de prix de consomation
							// W hazzina m3ana
							// montant+IndexCompteur+AncienIndexCompteur+validation
							Intent it = new Intent(IndexCompteur.this,
									Montant_Consomamtion.class);
							it.putExtra("montant", Montant);
							it.putExtra("IC", IndexCompteur.getText()
									.toString());
							it.putExtra("AIC", mt);
							it.putExtra("validation", val);
							startActivity(it);

						}

					}
				} catch (JSONException e) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							IndexCompteur.this);
					adb.setTitle("Vérification3");
					adb.setMessage("Index Compteur Incorrecte");
					adb.setPositiveButton("Ok", null);
					adb.show();
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
