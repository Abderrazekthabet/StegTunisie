package com.steg;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;






import com.steg.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Liste_Offre extends ListActivity {
	private static final String strURL = "http://10.0.2.2:8080/Steg_Tunisie/SelectOffre.php";
	
	String result = "";
	InputStream is;
	JSONArray ar;
	ListView maList;
	String [] Description_Offre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_offre);
		
		new SelectOffre().execute();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String selection = l.getItemAtPosition(position).toString();
		
		String SelectedDescription = Description_Offre[+position];
		finish();
		Intent it = new Intent(Liste_Offre.this,
				Description_Offre.class);
		it.putExtra("Description", SelectedDescription);
		startActivity(it);
		

	}

	public static String[] CreateArray(int size) {
		String c[] = new String[size];
		return c;
	}
	private class SelectOffre extends AsyncTask<Void, Void, JSONArray> {
		ProgressDialog mProgressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(Liste_Offre.this, "Progress",
					"En Cours ..", false, false);
		}

		@Override
		protected JSONArray doInBackground(Void... arg0) {

			JsonParser js = new JsonParser(strURL, null, null, null, null,
					null, null, null, null, null, null);
			js.initialisation( null, null, null, null, null, null, null,
					null, null, null);

			try {
				is = js.Connect(strURL);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(Liste_Offre.this);
				adb.setTitle("Info");
				adb.setMessage("Erreur Réseaux");
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
			try {
				result = js.ConvertToString(is);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(Liste_Offre.this);
				adb.setTitle("Vérifier");
				adb.setMessage("Aucunne Offre Disponible");
				adb.setPositiveButton("Ok", null);
				adb.show();
			}
			try {
				ar = js.Analyse(result);
			} catch (Exception e) {
				AlertDialog.Builder adb = new AlertDialog.Builder(Liste_Offre.this);
				adb.setTitle("Vérifier1");
				adb.setMessage("Aucunne Offre Disponible");
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
				AlertDialog.Builder adb = new AlertDialog.Builder(Liste_Offre.this);
				adb.setTitle("Vérifier2");
				adb.setMessage("Aucunne Offre Disponible");
				adb.setPositiveButton("Ok", null);
				adb.show();
			} else {
				try {
					String[] NombreOffre = CreateArray(result.length());
					 Description_Offre = CreateArray(result.length());
					 int j=1;
					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						String Desc_offre = json_data.getString("description");
						j+=i;
						Description_Offre[i]=Desc_offre;
						NombreOffre[i] = "Offre : "+ j ;
						
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							Liste_Offre.this, android.R.layout.simple_list_item_1,
							NombreOffre);
					setListAdapter(adapter);
				} catch (JSONException e) {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Liste_Offre.this);
					adb.setTitle("Vérifier3");
					adb.setMessage("Aucunne Offre Disponible");
					adb.setPositiveButton("Ok", null);
					adb.show();

				}

			}
		}

	}

}
