package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.gui;

import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R.layout;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R.menu;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.storage.VCardDataSource;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		Button button = (Button) findViewById(R.id.startScanActivityButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),CameraTestActivity.class);
				startActivity(intent);
			}
		});
		
		button = (Button) findViewById(R.id.StartStoredVCardsActivityButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),StoredBuissnessCardsListActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
}
