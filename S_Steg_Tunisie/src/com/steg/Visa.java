package com.steg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
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

public class Visa extends Activity {
	public static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/paieBancaire.php";
	EditText num, pass;
	Button valider;
	String Num, c, result = "";
	InputStream is;
	JSONArray ar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.visa);
		num = (EditText) findViewById(R.id.editText1);
		pass = (EditText) findViewById(R.id.editText2);
		valider = (Button) findViewById(R.id.button1);
		valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Num = num.getText().toString();
				c = pass.getText().toString();
				
					if (Num.length() != 16) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								Visa.this);

						adb.setTitle("Vérifier");
						adb.setMessage("Numero doit etre de 16 chiffre");
						adb.setPositiveButton("Ok", null);
						adb.show();
						num.requestFocus();
					} else if (c.length() != 4) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								Visa.this);
						adb.setTitle("Vérifier");

						adb.setMessage("Code doit etre de 4 chiffre");
						adb.setPositiveButton("Ok", null);
						adb.show();
						pass.requestFocus();
					} else if (num.getText().toString().equals("")) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								Visa.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Numero carte Obligatoire");
						adb.setPositiveButton("Ok", null);
						adb.show();
						num.requestFocus();
					} else if (pass.getText().toString().equals("")) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								Visa.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Code carte Obligatoire");
						adb.setPositiveButton("Ok", null);
						adb.show();
						pass.requestFocus();
					} else {
						
						try {
							
							long lg = Long.parseLong(Num);
							BigInteger NM = BigInteger.valueOf(lg);
							int ci = Integer.parseInt(c);
						new VisaPay().execute();
						} catch (Exception e) {

							AlertDialog.Builder adb1 = new AlertDialog.Builder(
									Visa.this);
							adb1.setTitle("Vérifier");
							adb1.setMessage("Code ou Numero doit etre Numérique");
							adb1.setPositiveButton("Ok", null);
							adb1.show();

							num.requestFocus();
						}

					}
				
			}
		});

	}

	private class VisaPay extends AsyncTask<Void, Void, JSONArray> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(Visa.this, "Progress",
					"En Cours ..", false, false);
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {

			JsonParser js = new JsonParser(strURL, Num, c, null, null, null,
					null, null, null, null, null);
			js.initialisation(Num, c, null, null, null, null, null, null, null,
					null);
			
			try {
				is=js.Connect(strURL);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						Visa.this);
				adb.setTitle("Vérifier");
				adb.setMessage("Erreur Réseaux");
				adb.setPositiveButton("Ok", null);
				adb.show();
			} 
			try{
			result=js.ConvertToString(is);
			}catch(Exception e){
				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						Visa.this);
				adb1.setTitle("Vérifier");
				adb1.setMessage("Code ou Numero Incorrecte");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
				num.requestFocus();
			}
			try{
			ar=js.Analyse(result);
			}catch(Exception e){
				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						Visa.this);
				adb1.setTitle("¨-¨");
				adb1.setMessage("Code Ou Numero Incorrecte");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
				pass.requestFocus();
			}
			return ar;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if(result==null){
				AlertDialog.Builder adb1 = new AlertDialog.Builder(
						Visa.this);
				adb1.setTitle("¨-¨");
				adb1.setMessage("Code Ou Numero Incorrecte");
				adb1.setPositiveButton("Ok", null);
				adb1.show();
			}else {
				
				try {
					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						String p = json_data.getString("motdepasse");
						String n = json_data
								.getString("NumeroBancaire");
						String s = json_data.getString("solde");
						if (p.equals(pass.getText().toString())
								&& n.equals(num.getText().toString())) {
							Intent it = new Intent(Visa.this,
									PaiementFactureVisa.class);
							it.putExtra("numeroBaicaire", n);
							it.putExtra("sol", s);
							startActivity(it);
							// *
						} else {
							Toast.makeText(Visa.this,
									"SVP Vérifier Vos cordonnées",
									Toast.LENGTH_LONG).show();
						}

					}
				} catch (JSONException e) {
					AlertDialog.Builder adb1 = new AlertDialog.Builder(
							Visa.this);
					adb1.setTitle("¨-¨");
					adb1.setMessage("Code Ou Numero Incorrecte");
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
