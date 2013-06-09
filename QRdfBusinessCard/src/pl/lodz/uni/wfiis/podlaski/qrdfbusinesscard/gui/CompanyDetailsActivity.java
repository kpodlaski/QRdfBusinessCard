package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.gui;

import java.util.ArrayList;

import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.QRdfBusinessCard;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R.layout;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R.menu;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.VCard;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.storage.VCardDataSource;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CompanyDetailsActivity extends Activity {

	private VCardDataSource dataSource;
	public final static String EXTRA_INTENT_PARAMETER_NAME = "vcard";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_details);
		dataSource = ((QRdfBusinessCard) getApplication()).getDatabase();
		
		//Get VCard object from previous Activity
		Bundle extras = getIntent().getExtras();
		if (extras == null) return;
		final VCard vcard = (VCard) extras.get(EXTRA_INTENT_PARAMETER_NAME);
		//Fill Form from vCard object
		((TextView) findViewById(R.id.NIP)).setText(vcard.getNip());
		((TextView) findViewById(R.id.REGON)).setText(vcard.getRegon());
		((TextView) findViewById(R.id.CompanyName)).setText(vcard.getName());
		((TextView) findViewById(R.id.CompanyEMail)).setText(vcard.getMail());
		((TextView) findViewById(R.id.CompanyAddress)).setText(String.format("%s %s, \nst.%s %d %d", 
																vcard.getPostalCode(), vcard.getLocality(),
																vcard.getStreet(), vcard.getHouseNumber(), vcard.getFlatNumber()));
		((TextView) findViewById(R.id.CompanyAccountNumber)).setText(vcard.getAccountNumber());
		((TextView) findViewById(R.id.CompanyBankName)).setText(vcard.getBankName());
		
 		
		//returnButton onClick 
		Button button = (Button) findViewById(R.id.returnButton);
		button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
                }
        });
		
		//sendEmailButton onClick 
		button = (Button) findViewById(R.id.sendEmailButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendEmailTo(vcard.getMail());
			}
		});
		
		//addToContactsButton onClick 
		button = (Button) findViewById(R.id.addToContactsButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToContacts(vcard);
				createToast("Card added to Contacts");
			}
		});

		  

		button = (Button) findViewById(R.id.addToCardsStore);
		//If vcard in Store change name of addToCardsStoreButton button
		if (vcard.getId()!=-1)
			button.setText(getString(R.string.RemoveFromStore));
		//addToCardsStoreButton onClick passing button object to be able change Text on button 		
		button.setOnClickListener(new OnClickListener() {
			private Button button;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (vcard.getId()==-1){
					dataSource.storeVCard(vcard);
					button.setText(getString(R.string.RemoveFromStore));
					createToast("Card added to Store");
				}
				else{ 
					dataSource.deleteVCard(vcard);
					button.setText(getString(R.string.AddToStore));
					createToast("Card removed from Store");
				}
				
			}
			private OnClickListener setButton(Button b)
			{
				button = b;
				return this;
			}
		}.setButton(button));
		
		//showCardsStoreButton onClick 		
		button = (Button) findViewById(R.id.showCardsStore);
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
		getMenuInflater().inflate(R.menu.company_details, menu);
		return true;
	}

	private void sendEmailTo(String mailAddress)
	{
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{mailAddress});		  
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}
	
	private void createToast(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	private void addToContacts(VCard vcard)
	{
		
		try {
		ArrayList<ContentProviderOperation> ops =
		            new ArrayList<ContentProviderOperation>();

		ContentProviderOperation.Builder op =
	            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
	            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
	            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
		 ops.add(op.build());
		 op =ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				 .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                 .withValue(ContactsContract.Data.MIMETYPE,
		                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
	             .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, vcard.getName());
	    ops.add(op.build());
	    // Sets the phone number and type
        //ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
        //.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
	    //.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
        //.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "11111111")
        //.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
	    //ops.add(op.build());
	    
	    op =ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
	    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
        .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, vcard.getMail())
	    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
	    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, vcard.getPostalCode())
        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, vcard.getLocality())
		.withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, String.format("%s %d %d",
          																		vcard.getStreet(),
        																		vcard.getHouseNumber(),
        																		vcard.getFlatNumber()));          
	    op.withYieldAllowed(true);
	    ops.add(op.build());
	    
	    op =ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)	    
	    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
	    .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
	    .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, String.format("%s %d %d",
																					vcard.getStreet(),
																					vcard.getHouseNumber(),
																					vcard.getFlatNumber()))
         .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
	     .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, vcard.getLocality())
	     .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
	     .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, vcard.getPostalCode())
	     .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
	     .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK);
	     ops.add(op.build());
		 
		 getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		 createToast("Contact added");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try
        {
            ContentResolver cr = this.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, vcard.getName());
            //cv.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "1234567890");
            //cv.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            cv.put(ContactsContract.CommonDataKinds.Email.ADDRESS, vcard.getMail());
            cv.put(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, vcard.getPostalCode());
            cv.put(ContactsContract.CommonDataKinds.StructuredPostal.CITY, vcard.getLocality());
            cv.put(ContactsContract.CommonDataKinds.StructuredPostal.STREET, String.format("%s %d %d",
            																		vcard.getStreet(),
            																		vcard.getHouseNumber(),
            																		vcard.getFlatNumber()));
            
            cr.insert(ContactsContract.Data.CONTENT_URI, cv);
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        */
        
	}
}
