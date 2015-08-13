package com.steg;

import java.io.IOException;
import java.io.InputStream;
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

import com.steg.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Inscrie extends Activity {
	EditText nom, prenom, cin, tel, motdepasse, login;
	Button Ajouter;
	int num, numCin;
	String NTel, NCin, Nom, Prenom, Motdepasse, Login;
	private static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/Inscription.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inscrie);

		nom = (EditText) findViewById(R.id.editText2);
		cin = (EditText) findViewById(R.id.editText1);
		prenom = (EditText) findViewById(R.id.editText3);
		tel = (EditText) findViewById(R.id.editText4);
		motdepasse = (EditText) findViewById(R.id.editText5);
		login = (EditText) findViewById(R.id.editText6);
		Ajouter = (Button) findViewById(R.id.button1);

		Ajouter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				/********************************************/
				if(cin.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Inscrie.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Cin Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					cin.requestFocus();

				} else if (nom.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Inscrie.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Nom Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					nom.requestFocus();

				} else if (prenom.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Inscrie.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Prenom Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					prenom.requestFocus();

				} else if (tel.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Inscrie.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Numero Tel Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					tel.requestFocus();
				} else if (motdepasse.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Inscrie.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Mot De Passe Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					motdepasse.requestFocus();

				} else if (login.getText().toString().equals("")) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Inscrie.this);
					adb.setTitle("Vérifier");
					adb.setMessage("Login Obligatoire");
					adb.setPositiveButton("Ok", null);
					adb.show();
					login.requestFocus();

				} else {
					Nom = nom.getText().toString();
					Prenom = prenom.getText().toString();
					Motdepasse = motdepasse.getText().toString();
					Login = login.getText().toString();
					try {
						NTel = tel.getText().toString();
						num = Integer.parseInt(NTel);
						/*************************************************/
						try {
							NCin = cin.getText().toString();
							numCin = Integer.parseInt(NCin);

							if ( NTel.length() != 8) {

								AlertDialog.Builder adb = new AlertDialog.Builder(
										Inscrie.this);
								adb.setTitle("Vérification");
								adb.setMessage("Vérifier Votre Numero tel");
								adb.setPositiveButton("Ok", null);
								adb.show();
								tel.requestFocus();
							} else if (NCin.length() != 8) {

								AlertDialog.Builder adb = new AlertDialog.Builder(
										Inscrie.this);
								adb.setTitle("Vérification");
								adb.setMessage("Vérifier Votre CIN");
								adb.setPositiveButton("Ok", null);
								adb.show();
								cin.requestFocus();
							} else {
								new InscrieClient().execute();

							}
						} catch (Exception e) {

							AlertDialog.Builder adb = new AlertDialog.Builder(
									Inscrie.this);
							adb.setTitle("Vérification");
							adb.setMessage("Cin doit étre Numérique");
							adb.setPositiveButton("Ok", null);
							adb.show();
							cin.requestFocus();
						}
					} catch (Exception e) {

						AlertDialog.Builder adb = new AlertDialog.Builder(
								Inscrie.this);
						adb.setTitle("Vérification");
						adb.setMessage("Numero Tel doit étre Numérique");
						adb.setPositiveButton("Ok", null);
						adb.show();
						tel.requestFocus();
					}

				}
			}
		});
	}

	private class InscrieClient extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(Inscrie.this, "Progress",
					"Inscrie en cours..", false, false);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			JsonParser js = new JsonParser(strURL, NCin, Nom, Prenom, NTel,
					Motdepasse, Login, null, null, null, null);

			js.initialisation(NCin, Nom, Prenom, NTel, Motdepasse, Login, null,
					null, null, null);

			try {
				js.Connect(strURL);
				return true;
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(Inscrie.this);
				adb.setTitle("Info");
				adb.setMessage("Erreur Réseaux");
				adb.setPositiveButton("Ok", null);
				adb.show();
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();

			if (result) {
				AlertDialog.Builder adb = new AlertDialog.Builder(Inscrie.this);
				adb.setTitle("Info");
				adb.setMessage("Incription terminer avec succée");
				adb.setPositiveButton("Ok", null);
				adb.show();
			} else {
				AlertDialog.Builder adb = new AlertDialog.Builder(Inscrie.this);
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
