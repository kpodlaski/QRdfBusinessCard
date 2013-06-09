package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.gui;

import java.util.List;

import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.QRdfBusinessCard;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R.layout;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R.menu;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.VCard;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.storage.VCardDataSource;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StoredBuissnessCardsListActivity extends Activity {

	private VCardDataSource dataSource;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stored_buissness_cards_list);
		dataSource = ((QRdfBusinessCard) getApplication()).getDatabase();
		List<VCard> vCards = dataSource.getAllVCards();
		ArrayAdapter<VCard> listViewAdapter = new ArrayAdapter<VCard>(this, R.layout.list_item,vCards);
		TextView counter = (TextView) findViewById(R.id.StoredCardsCounter);
		counter.setText("Stored Business Cards: "+vCards.size());
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(listViewAdapter);
		
		//ListView element OnClick
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View View, int position,
					long id) {
				// TODO Auto-generated method stub
				final VCard vCard = (VCard) adapter.getItemAtPosition(position);
				Intent intent = new Intent(getApplicationContext(), CompanyDetailsActivity.class);
				intent.putExtra(CompanyDetailsActivity.EXTRA_INTENT_PARAMETER_NAME, vCard);
				startActivity(intent);
			}
		});
		
		
		//Back Button OnClick
		Button button = (Button) findViewById(R.id.Back);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stored_buissness_cards_list, menu);
		return true;
	}

	
}
