package com.steg;

import com.steg.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Description_Offre extends Activity {
	String Description;
	TextView description;
	Button quitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offre);
		description=(TextView)findViewById(R.id.textView1);
		quitter = (Button) findViewById(R.id.button1);

		quitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
				Intent i=new Intent(Description_Offre.this,Liste_Offre.class);
				startActivity(i);

			}
		});
		Intent data = this.getIntent();
		Description = data.getExtras().getString("Description");
		
		description.setText(Description);
	}

}
