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

public class Identifier extends Activity {
	EditText Login, MotDePasse;
	Button valider;
	String login, Motdepasse, result = "";
	InputStream is;
	JSONArray ar;
	private static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/connexion.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.identifier);
		Login = (EditText) findViewById(R.id.editText1);
		MotDePasse = (EditText) findViewById(R.id.editText2);
		valider = (Button) findViewById(R.id.button1);

		valider.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (Login.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Identifier.this);
					adb.setTitle("Vérifier");
					adb.setMessage("LOGIN Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					Login.requestFocus();

				} else if (MotDePasse.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Identifier.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Mot De Passe Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					MotDePasse.requestFocus();

				} else {
					login=Login.getText().toString();
					Motdepasse=MotDePasse.getText().toString();
					new IdentfierClient().execute();
				}

			}
		});
	}

	private class IdentfierClient extends AsyncTask<Void, Void, JSONArray> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(Identifier.this, "Progress",
					"En Cours ..", false, false);
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {

			JsonParser js = new JsonParser(strURL, login, Motdepasse, null,
					null, null, null, null, null, null, null);
			js.initialisation(login, Motdepasse, null, null, null, null, null,
					null, null, null);

			try {
				is = js.Connect(strURL);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						Identifier.this);
				adb.setTitle("Info");
				adb.setMessage("Erreur Réseaux");
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
			try {
				result = js.ConvertToString(is);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						Identifier.this);
				adb.setTitle("Vérifier");
				adb.setMessage("Login Ou Mot de passe Incorrecte");
				adb.setPositiveButton("Ok", null);
				adb.show();
				Login.requestFocus();
			}
			try {
				ar = js.Analyse(result);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(
						Identifier.this);
				adb.setTitle("Vérifier1");
				adb.setMessage("Login Ou Mot De passe Incorrecte");
				adb.setPositiveButton("Ok", null);
				adb.show();
				Login.requestFocus();
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
						Identifier.this);
				adb.setTitle("Vérifier2");
				adb.setMessage("Login Ou Mot De passe Incorrecte");
				adb.setPositiveButton("Ok", null);
				adb.show();
				Login.requestFocus();
			} else {
				try {

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						String login = json_data.getString("login");
						String pss = json_data.getString("motdepasse");

						if (login.equals(Login.getText().toString())
								&& pss.equals(MotDePasse.getText().toString())) {
							Intent it = new Intent(Identifier.this,
									Compte.class);
							startActivity(it);

						}

					}
				} catch (JSONException e) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Identifier.this);
					adb.setTitle("Vérifier3");
					adb.setMessage("Login Ou PWD Incorrecte");
					adb.setPositiveButton("Ok", null);
					adb.show();
					Login.requestFocus();
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
