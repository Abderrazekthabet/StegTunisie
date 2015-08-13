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

public class ConsommationElec extends Activity {
	private static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/SelectFacture.php";
	EditText Cin;
	Button valider;
	String NumCin, result = "";
	InputStream is;
	JSONArray ar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consommation);

		Cin = (EditText) findViewById(R.id.editText1);
		valider = (Button) findViewById(R.id.button1);

		valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				try {
					NumCin = Cin.getText().toString();
					int ci = Integer.parseInt(NumCin);
					if (NumCin.length() != 8) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								ConsommationElec.this);
						adb.setTitle("Vérifier");
						adb.setMessage("Cin doit etre de 8 chiffre");
						adb.setPositiveButton("Ok", null);
						adb.show();
						Cin.requestFocus();
					} else {
						new SelectFacture().execute();
						
					}
				} catch (Exception e) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							ConsommationElec.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Cin doit etre Numérique");
					adb.setPositiveButton("Ok", null);
					adb.show();
					Cin.requestFocus();
				}

			}
		});
	}

	private class SelectFacture extends AsyncTask<Void, Void, JSONArray> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(ConsommationElec.this,
					"Progress", "Enc cours ..", false, false);
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JsonParser js = new JsonParser(strURL, NumCin, null, null, null,
					null, null, null, null, null, null);
			js.initialisation(NumCin, null, null, null, null, null, null, null,
					null, null);

			try {
				is = js.Connect(strURL);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						ConsommationElec.this);
				adb.setTitle("Info");
				adb.setMessage("Erreur Réseaux");
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
			try {
				result = js.ConvertToString(is);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						ConsommationElec.this);
				adb.setTitle("Vérification");
				adb.setMessage("Cin Incorrecte");
				adb.setPositiveButton("Ok", null);
				adb.show();
				Cin.requestFocus();
			}
			try {
				ar = js.Analyse(result);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						ConsommationElec.this);
				adb.setTitle("Vérification");
				adb.setMessage("Cin Incorrecte");
				adb.setPositiveButton("Ok", null);
				adb.show();
				Cin.requestFocus();
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
						ConsommationElec.this);
				adb.setTitle("Vérification");
				adb.setMessage("Cin Incorrecte");
				adb.setPositiveButton("Ok", null);
				adb.show();
				Cin.requestFocus();
			} else {
				try {

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						String mt = json_data.getString("solde");
						String val = json_data.getString("validation");
						
						Intent it = new Intent(ConsommationElec.this,
								IndexCompteur.class);
						it.putExtra("montant", mt);
						it.putExtra("validation", val);
						it.putExtra("cin", Cin.getText().toString());
						startActivity(it);

					}
				} catch (JSONException e) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							ConsommationElec.this);
					adb.setTitle("Vérification");
					adb.setMessage("Cin Incorrecte");
					adb.setPositiveButton("Ok", null);
					adb.show();
					Cin.requestFocus();
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
