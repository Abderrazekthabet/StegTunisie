package com.steg;



import com.steg.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Montant_Consomamtion extends Activity {
	TextView tv;
	Button menu;
	String PrixCons = "";
	int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.montant_consommation);
		tv = (TextView) findViewById(R.id.textView2);
		menu = (Button) findViewById(R.id.button1);
		/****************************************************************/
		// 9belna les données
		// montant+indexCompteur+AncienIndexCompteur+validation
		Intent data = this.getIntent();
		final String Montant = data.getExtras().getString("montant");
		final String indexCompteur = data.getExtras().getString("IC");
		final String AncienindexCompteur = data.getExtras().getString("AIC");
		final String validation = data.getExtras().getString("validation");
		/**************************************************************************/
		// ma8 nconvertou les Strings en Integer
		// montant +indexCompteur+AncienIndexCompteur
		// et en va faire la déferrence
		Float M = Float.parseFloat(Montant);
		int ic = Integer.parseInt(indexCompteur);
		int aic = Integer.parseInt(AncienindexCompteur);
		int def_consommation = ic - aic;
		/*********************************************************************/
		// lenna 3adna déférrence ma8 ne7sbou prix de consommation
		// w supposina elli soum el 1 kilo 0.110
		Double MontantConsommation = def_consommation * 0.110;
		String Resultat = String.valueOf(MontantConsommation);

		/************ Position Du (.) ********************************/
		for (int j = 0; j < Resultat.length(); j++) {
			if (Resultat.charAt(j) == '.') {
				position = j;
			}
		}
		/*********************************************************/
		// laHna 3adna deux cas fel validation elli houma facture 5alsa w
		// facture mech 5alsa
		// si validation == oui : ma3neha facture 5alsa donc ne7sbou ken prix de
		// consommation de déferrence
		// sinon ma3neha validation == non : facture me8 5alsa donc ne7sbou prix
		// de deference w nzidouh solde de facture
		if (validation.equals("oui")) {

			if ((position == 1) && (Resultat.length() < 4)) {

				while (Resultat.length() <= 4) {
					Resultat += "0";
				}
			} else if ((position == 2) && (Resultat.length() < 5)) {

				while (Resultat.length() <= 5) {
					Resultat += "0";
				}
			} else if ((position == 3) && (Resultat.length() < 6)) {

				while (Resultat.length() <= 6) {
					Resultat += "0";
				}
			}

			else if ((position == 4) && (Resultat.length() < 7)) {

				while (Resultat.length() <= 7) {
					Resultat += "0";
				}
			}
			tv.setText(Resultat);
		} else {

			if ((position == 1) && (Resultat.length() < 4)) {

				while (Resultat.length() <= 4) {
					Resultat += "0";
				}
			} else if ((position == 2) && (Resultat.length() < 5)) {

				while (Resultat.length() <= 5) {
					Resultat += "0";
				}
			} else if ((position == 3) && (Resultat.length() < 6)) {

				while (Resultat.length() <= 6) {
					Resultat += "0";
				}
			}

			else if ((position == 4) && (Resultat.length() < 7)) {

				while (Resultat.length() <= 7) {
					Resultat += "0";
				}
			}
			Float prix = Float.parseFloat(Resultat);

			Float P = prix + M;
			String ResultatFinal = String.valueOf(P);
			tv.setText(ResultatFinal);

		}

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent im = new Intent(Montant_Consomamtion.this, Compte.class);
				startActivity(im);
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
