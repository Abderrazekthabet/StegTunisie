package com.steg;


import java.io.InputStream;

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


public class PaiementFactureEdinar extends Activity {
	EditText org, ref, mt;
	Button valider;
	String sol,Ref, Num, MT, result = "", mtt;
	InputStream is;
	JSONArray ar;
	public static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/MontantFacture.php";
	public static final String strURL1 = "http://10.0.2.2:8080/Steg_Tunisie/FACTUREEDINAR.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paiement_facture_edinar);

		Intent data = this.getIntent();
		Num = data.getExtras().getString("numeroEdinar");
		sol = data.getExtras().getString("sol");
		org = (EditText) findViewById(R.id.editText1);
		ref = (EditText) findViewById(R.id.editText2);
		mt = (EditText) findViewById(R.id.editText3);
		valider = (Button) findViewById(R.id.button1);

		valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Ref = ref.getText().toString();
				MT = mt.getText().toString();
				try {
					Double s = Double.parseDouble(MT);
					Double s1 = Double.parseDouble(sol);
					/***************************************/
					String r = ref.getText().toString();
					String o = org.getText().toString();
					if (s1 < s) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureEdinar.this);
						adb.setTitle("Vérifier");
						adb.setMessage("solde insuffisant");
						adb.setPositiveButton("Ok", null);
						adb.show();
					} else if (r.length() != 8) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureEdinar.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Ref Facture doit etre de 8 chiffre");
						adb.setPositiveButton("Ok", null);
						adb.show();
						ref.requestFocus();
					} else if (o.length() != 3) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureEdinar.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Org doit etre de 3 chiffre");
						adb.setPositiveButton("Ok", null);
						adb.show();
						org.requestFocus();

					} else if (org.getText().toString().equals("")) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureEdinar.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Organisme Obligatoire");
						adb.setPositiveButton("Ok", null);
						adb.show();
						org.requestFocus();

					} else if (ref.getText().toString().equals("")) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureEdinar.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Reference Obligatoire");
						adb.setPositiveButton("Ok", null);
						adb.show();
						ref.requestFocus();

					} else if (mt.getText().toString().equals("")) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								PaiementFactureEdinar.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Montant Obligatoire");
						adb.setPositiveButton("Ok", null);
						adb.show();
						mt.requestFocus();

					} else {
						/********************************************/
						new VerifMontantFacture().execute();
						/******************************************/

					}
				} catch (Exception e) {

					AlertDialog.Builder adb1 = new AlertDialog.Builder(
							PaiementFactureEdinar.this);
					adb1.setTitle("Vérifier");
					adb1.setMessage("Ref ou code Facture doit etre Numérique");
					adb1.setPositiveButton("Ok", null);
					adb1.show();
					ref.requestFocus();
				}

			}
		});

	}

	private class VerifMontantFacture extends AsyncTask<Void, Void, JSONArray> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(PaiementFactureEdinar.this,
					"Progress", "Verification En cour..", false, false);
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JsonParser js = new JsonParser(strURL, Ref, null, null, null, null,
					null, null, null, null, null);
			js.initialisation(Ref, null, null, null, null, null, null, null,
					null, null);
			try {
				is = js.Connect(strURL);
			} catch (Exception e) {
				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						PaiementFactureEdinar.this);
				adb1.setTitle("Vérifier");
				adb1.setMessage("Erreur Réseaux");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
			}
			try {
				result = js.ConvertToString(is);
			} catch (Exception e) {
				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						PaiementFactureEdinar.this);
				adb1.setTitle("Vérifier");
				adb1.setMessage("Ref Incorrecte");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
			}
			try {
				ar = js.Analyse(result);
			} catch (Exception e) {
				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						PaiementFactureEdinar.this);
				adb1.setTitle("Vérifier");
				adb1.setMessage("Reference facture Incorrecte");
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
				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						PaiementFactureEdinar.this);
				adb1.setTitle("Vérifier");
				adb1.setMessage("Ref Facture Incorrecte");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
			} else {

				try {
					
					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);

						mtt = json_data.getString("solde");
						String mont = mt.getText().toString();
						Double montantSaisie = Double.parseDouble(mont);
						Double montantFacture = Double.parseDouble(mtt);

						if ((montantSaisie > montantFacture)
								|| (montantSaisie < montantFacture)) {
							AlertDialog.Builder adb2 = new AlertDialog.Builder(
									PaiementFactureEdinar.this);
							adb2.setTitle("Vérification");
							adb2.setMessage("montant du Facture est: " + mtt);
							adb2.setPositiveButton("Ok", null);
							adb2.show();
							mt.requestFocus();
						}
						if (montantSaisie.equals(montantFacture)) {

							new PaiementFacture().execute();
						}

					}

				} catch (JSONException e1) {
					AlertDialog.Builder adb2 = new AlertDialog.Builder(
							PaiementFactureEdinar.this);
					adb2.setTitle("Vérifier");
					adb2.setMessage("Ref Facture Incorrecte");
					adb2.setPositiveButton("Ok", null);
					adb2.show();
					ref.requestFocus();
				}

			}
		}
	}

	private class PaiementFacture extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(PaiementFactureEdinar.this,
					"Progress", "Paiement en cour ..", false, false);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			JsonParser js = new JsonParser(strURL1, Num, Ref, MT, null, null,
					null, null, null, null, null);
			js.initialisation(Num, Ref, MT, null, null, null, null, null, null,
					null);

			try {
				js.Connect(strURL1);
				return true;
			} catch (Exception e) {
				AlertDialog.Builder adb2 = new AlertDialog.Builder(
						PaiementFactureEdinar.this);
				adb2.setTitle("vérification");
				adb2.setMessage("montant du Facture est: " + mtt);
				adb2.setPositiveButton("Ok", null);
				adb2.show();
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
						PaiementFactureEdinar.this);
				adb2.setTitle("Vérification");
				adb2.setMessage("Facture payer avec succées");
				adb2.setPositiveButton("Ok", null);
				adb2.show();
			} else {
				AlertDialog.Builder adb2 = new AlertDialog.Builder(
						PaiementFactureEdinar.this);
				adb2.setTitle("Vérification");
				adb2.setMessage("Vérifier Vos Données ");
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
