package com.steg;

import com.steg.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	Button Demmande, Reclamation, Facture, Appel, Localisation, Compte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Demmande = (Button) findViewById(R.id.button1);
		Reclamation = (Button) findViewById(R.id.button2);
		Facture = (Button) findViewById(R.id.button3);
		Compte = (Button) findViewById(R.id.button4);
		Appel = (Button) findViewById(R.id.button5);
		Localisation = (Button) findViewById(R.id.button6);

		Demmande.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Demmande.class);
				startActivity(i);

			}
		});

		Reclamation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Reclamation.class);
				startActivity(i);
			}
		});

		Facture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Facture.class);
				startActivity(i);
			}
		});

		Appel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_CALL);
				i.setData(Uri.parse("tel:77234777"));
				startActivity(i);

			}
		});

		Localisation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Localisation.class);
				startActivity(i);
			}
		});

		Compte.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, Connexion.class);
				startActivity(i);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
