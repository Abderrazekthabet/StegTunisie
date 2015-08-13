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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PaiementFactureVisa extends Activity {
	public static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/MontantFacture.php";
	public static final String strURL1 = "http://10.0.2.2:8080/Steg_Tunisie/FactureBancaire.php";
	EditText org, ref, mt;
	Button valider;
	String mtt, REF, ORG, MT, Num, result = "";
	InputStream is;
	JSONArray ar;
	double resMt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paiement_facture_visa);
		Intent data = this.getIntent();
		Num = data.getExtras().getString("numeroBaicaire");
		final String sol = data.getExtras().getString("sol");
		org = (EditText) findViewById(R.id.editText1);
		ref = (EditText) findViewById(R.id.editText2);
		mt = (EditText) findViewById(R.id.editText3);
		valider = (Button) findViewById(R.id.button1);
		valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				REF = ref.getText().toString();
				MT = mt.getText().toString();

				try {
					Double s = Double.parseDouble(mt.getText().toString());
					Double s1 = Double.parseDouble(sol);
					/***************************************/
					String r = ref.getText().toString();
					String o = org.getText().toString();
					if (s1 < s) {
						Toast.makeText(PaiementFactureVisa.this,
								"Solde Insufésant", Toast.LENGTH_LONG).show();

					} else if (r.length() != 8) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureVisa.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Ref Facture doit etre de 8 chiffre");
						adb.setPositiveButton("Ok", null);
						adb.show();
						ref.requestFocus();
					} else if (o.length() != 3) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureVisa.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Org doit etre de 3 chiffre");
						adb.setPositiveButton("Ok", null);
						adb.show();
						org.requestFocus();

					} else if (org.getText().toString().equals("")) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureVisa.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Org Obligatoire");
						adb.setPositiveButton("Ok", null);
						adb.show();
						org.requestFocus();

					} else if (ref.getText().toString().equals("")) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureVisa.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Ref Obligatoire");
						adb.setPositiveButton("Ok", null);
						adb.show();
						ref.requestFocus();

					} else if (mt.getText().toString().equals("")) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureVisa.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Montant Obligatoire");
						adb.setPositiveButton("Ok", null);
						adb.show();
						mt.requestFocus();

					} else {
						resMt=s1-s;
						MT = Double.toString(resMt);
						new PaiementVisa().execute();

					}
				} catch (Exception e) {

					AlertDialog.Builder adb1 = new AlertDialog.Builder(
							PaiementFactureVisa.this);
					adb1.setTitle("Vérifier");
					adb1.setMessage("Ref ou code Facture doit etre Numérique");
					adb1.setPositiveButton("Ok", null);
					adb1.show();
					ref.requestFocus();
				}

			}
		});

	}

	private class PaiementVisa extends AsyncTask<Void, Void, JSONArray> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(PaiementFactureVisa.this,
					"Progress", "En Cours ..", false, false);
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JsonParser js = new JsonParser(strURL, REF, null, null, null, null,
					null, null, null, null, null);
			js.initialisation(REF, null, null, null, null, null, null, null,
					null, null);

			try {
				is = js.Connect(strURL);
			} catch (Exception e) {
				Toast.makeText(PaiementFactureVisa.this,
						"Error in http connection ", Toast.LENGTH_LONG).show();
			}

			try {
				result = js.ConvertToString(is);
			} catch (Exception e) {
				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						PaiementFactureVisa.this);
				adb1.setTitle("Vérifier");
				adb1.setMessage("Ref ou Code Facture Incorrecte");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
			}

			try {
				ar = js.Analyse(result);
			} catch (Exception e) {
				AlertDialog.Builder adb2 = new AlertDialog.Builder(
						PaiementFactureVisa.this);
				adb2.setTitle("Vérifier");
				adb2.setMessage("Ref ou Code Facture Incorrecte");
				adb2.setPositiveButton("Ok", null);
				adb2.show();
				ref.requestFocus();
			}
			return ar;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if (result == null) {
				AlertDialog.Builder adb2 = new AlertDialog.Builder(
						PaiementFactureVisa.this);
				adb2.setTitle("Vérifier");
				adb2.setMessage("Ref ou Code Facture Incorrecte");
				adb2.setPositiveButton("Ok", null);
				adb2.show();
				ref.requestFocus();
			} else {

				try {

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);

						mtt = json_data.getString("solde");
						String mont = mt.getText().toString();// montant
						Double lgg = Double.parseDouble(mont);
						Double lggg = Double.parseDouble(mtt);

						if ((lgg > lggg) || (lgg < lggg)) {
							AlertDialog.Builder adb2 = new AlertDialog.Builder(
									PaiementFactureVisa.this);
							adb2.setTitle("^-^");
							adb2.setMessage("montant du Facture est: " + mtt);
							adb2.setPositiveButton("Ok", null);
							adb2.show();
							mt.requestFocus();
						}
						if (lgg.equals(lggg)) {
							new PaiementSuccess().execute();

						}

					}

				} catch (JSONException e1) {
					AlertDialog.Builder adb2 = new AlertDialog.Builder(
							PaiementFactureVisa.this);
					adb2.setTitle("Vérifier");
					adb2.setMessage("Ref ou Code Facture Incorrecte");
					adb2.setPositiveButton("Ok", null);
					adb2.show();
					ref.requestFocus();
				}

			}
		}

	}

	private class PaiementSuccess extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(PaiementFactureVisa.this,
					"Progress", "En Cours ..", false, false);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			JsonParser js = new JsonParser(strURL1, Num, REF, MT, null, null,
					null, null, null, null, null);
			js.initialisation(Num, REF, MT, null, null, null, null, null, null,
					null);
			try {
				js.Connect(strURL1);
				return true;
			} catch (Exception e) {
				Toast.makeText(PaiementFactureVisa.this,
						"Error in http connection ", Toast.LENGTH_LONG).show();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if (result) {
				AlertDialog.Builder adb2 = new AlertDialog.Builder(
						PaiementFactureVisa.this);
				adb2.setTitle("^-^");
				adb2.setMessage("Facture paié avec succées");
				adb2.setPositiveButton("Ok", null);
				adb2.show();
			} else {
				AlertDialog.Builder adb2 = new AlertDialog.Builder(
						PaiementFactureVisa.this);
				adb2.setTitle("^-^");
				adb2.setMessage("montant du Facture est: " + mtt);
				adb2.setPositiveButton("Ok", null);
				adb2.show();
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
