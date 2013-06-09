package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.storage;

import java.util.ArrayList;
import java.util.List;

import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R.id;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.VCard;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class VCardDataSource {
	//Database acess
	private SQLiteDatabase db;
	private MySQLitleDatabaseHelper dbHelper;
	private boolean oppened = false;
	
	public VCardDataSource(Context context)
	{
		dbHelper = new MySQLitleDatabaseHelper(context);
	}
	
	public void open(){
		db = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void storeVCard(VCard vcard)
	{
		if (!oppened) open();
		boolean update = false;
		if (vcard.getId() != -1) update=true;
		//Check if there is in the Store Companies with the same Regon and Nip
		List<VCard> identical = getVCardsForNipAndRegon(vcard.getNip(), vcard.getRegon());
		System.out.println(identical.size());
		if (identical.size()==1)
		{
			vcard.setId(identical.get(0).getId());
			update = true;
		}
		ContentValues val = new ContentValues();
		val.put(VCardTable.COLUMN_NAME, vcard.getName());
		val.put(VCardTable.COLUMN_NIP, vcard.getNip());
		val.put(VCardTable.COLUMN_REGON, vcard.getRegon());
		val.put(VCardTable.COLUMN_BANK_NAME, vcard.getBankName());
		val.put(VCardTable.COLUMN_ACCOUNT_NUMBER, vcard.getAccountNumber());
		val.put(VCardTable.COLUMN_MAILTO, vcard.getMailto());
		val.put(VCardTable.COLUMN_STREET, vcard.getStreet());
		val.put(VCardTable.COLUMN_FLAT_NUMBER, vcard.getFlatNumber());
		val.put(VCardTable.COLUMN_HOUSE_NUMBER, vcard.getHouseNumber());
		val.put(VCardTable.COLUMN_LOCALITY, vcard.getLocality());
		val.put(VCardTable.COLUMN_POST_OFFICE, vcard.getPostOffice());
		val.put(VCardTable.COLUMN_POSTAL_CODE, vcard.getPostalCode());
		val.put(VCardTable.COLUMN_IN_CONTACTS, 0);
		val.put(VCardTable.COLUMN_URI, vcard.getUri());
		if (update){
			System.out.println("update");
			Log.w(VCardDataSource.class.getName(), "Update record with name:"+vcard.getName()+" and id:"+vcard.getId());
			db.update(VCardTable.VCARD_TABLE, val, VCardTable.COLUMN_ID +"="+vcard.getId(), null);
		}
		else{
			Log.w(VCardDataSource.class.getName(), "Store record with name:"+vcard.getName());
			vcard.setId(db.insert(VCardTable.VCARD_TABLE, null, val));
			System.out.println("insert");
		}
	}
	
	public void deleteVCardById(long id)
	{
		if (!oppened) open();
		Log.w(VCardDataSource.class.getName(), "Delete record id="+id);
		db.delete(VCardTable.VCARD_TABLE, VCardTable.COLUMN_ID +"="+id, null);
	}
	
	public void deleteVCard(VCard vcard)
	{
		if (!oppened) open();
		deleteVCardById(vcard.getId());
		vcard.setId(-1);
	}
	
	public void vCardAddedToContacts(VCard vcard)
	{
		if (!oppened) open();
		if (vcard.getId()==-1) storeVCard(vcard);
		ContentValues val = new ContentValues();
		val.put(VCardTable.COLUMN_IN_CONTACTS, 1);
		db.update(VCardTable.VCARD_TABLE, val, VCardTable.COLUMN_ID + "=" +vcard.getId(), null);
		vcard.setInContacts(true);
	}
	
	public void vCardRemovedFromContacts(VCard vcard)
	{
		if (!oppened) open();
		ContentValues val = new ContentValues();
		val.put(VCardTable.COLUMN_IN_CONTACTS, 0);
		db.update(VCardTable.VCARD_TABLE, val, VCardTable.COLUMN_ID + "=" +vcard.getId(), null);
		vcard.setInContacts(false);
	}
	
	public List<VCard> getAllVCards()
	{
		if (!oppened) open();
		List<VCard> results = new ArrayList<VCard>();
		Cursor cursor = db.query(VCardTable.VCARD_TABLE, VCardTable.allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			results.add(vCardFromCursor(cursor));
			cursor.moveToNext();
		}
		return results;
	}
	
	public List<VCard> getVCardsFromContacts()
	{
		if (!oppened) open();
		List<VCard> results = new ArrayList<VCard>();
		Cursor cursor = db.query(VCardTable.VCARD_TABLE, VCardTable.allColumns, VCardTable.COLUMN_IN_CONTACTS +"=1", null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			results.add(vCardFromCursor(cursor));
			cursor.moveToNext();
		}
		return results;
	}
	
	public List<VCard> getVCardsForName(String name)
	{
		if (!oppened) open();
		List<VCard> results = new ArrayList<VCard>();
		Cursor cursor = db.query(VCardTable.VCARD_TABLE, VCardTable.allColumns, VCardTable.COLUMN_NAME +"='"+name+"'", null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			results.add(vCardFromCursor(cursor));
			cursor.moveToNext();
		}
		return results;
	}

	public List<VCard> getVCardsForNip(String nip)
	{
		if (!oppened) open();
		List<VCard> results = new ArrayList<VCard>();
		Cursor cursor = db.query(VCardTable.VCARD_TABLE, VCardTable.allColumns, VCardTable.COLUMN_NIP +"='"+nip+"'", null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			results.add(vCardFromCursor(cursor));
			cursor.moveToNext();
		}
		return results;
	}
	
	public List<VCard> getVCardsForRegon(String regon)
	{
		if (!oppened) open();
		List<VCard> results = new ArrayList<VCard>();
		Cursor cursor = db.query(VCardTable.VCARD_TABLE, VCardTable.allColumns, VCardTable.COLUMN_REGON +"='"+regon+"'", null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			results.add(vCardFromCursor(cursor));
			cursor.moveToNext();
		}
		return results;
	}
	
	public List<VCard> getVCardsForNipAndRegon(String nip, String regon)
	{
		if (!oppened) open();
		List<VCard> results = new ArrayList<VCard>();
		Cursor cursor = db.query(VCardTable.VCARD_TABLE, VCardTable.allColumns, VCardTable.COLUMN_NIP +"='"+nip+ "' AND "+VCardTable.COLUMN_REGON +"='"+regon+"'" , null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			results.add(vCardFromCursor(cursor));
			cursor.moveToNext();
		}
		return results;
	}
	
	public List<VCard> getVCardsForURI(String uri)
	{
		if (!oppened) open();
		List<VCard> results = new ArrayList<VCard>();
		Cursor cursor = db.query(VCardTable.VCARD_TABLE, VCardTable.allColumns, VCardTable.COLUMN_URI +"='"+uri+"'", null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			results.add(vCardFromCursor(cursor));
			cursor.moveToNext();
		}
		return results;
	}

	/* columns order 
	 * COLUMN_ID , 
	 * COLUMN_NAME, COLUMN_NIP, COLUMN_REGON, 
	 * COLUMN_BANK_NAME, COLUMN_ACCOUNT_NUMBER, 
	 * COLUMN_MAILTO, 
	 * COLUMN_STREET, COLUMN_FLAT_NUMBER, COLUMN_HOUSE_NUMBER, 
	 * COLUMN_LOCALITY, COLUMN_POSTAL_CODE, COLUMN_POST_OFFICE, 
	 * COLUMN_IN_CONTACTS,
	 * COLUMN_URI
	 */
	private VCard vCardFromCursor(Cursor cursor) {
		VCard vcard = new VCard();
		vcard.setId(cursor.getLong(0));
		vcard.setName(cursor.getString(1));
		vcard.setNip(cursor.getString(2));
		vcard.setRegon(cursor.getString(3));
		vcard.setBankName(cursor.getString(4));
		vcard.setAccountNumber(cursor.getString(5));
		vcard.setMailto(cursor.getString(6));
		vcard.setStreet(cursor.getString(7));
		vcard.setFlatNumber(cursor.getInt(8));
		vcard.setHouseNumber(cursor.getInt(9));
		vcard.setLocality(cursor.getString(10));
		vcard.setPostalCode(cursor.getString(11));
		vcard.setPostOffice(cursor.getString(12));
		vcard.setInContacts(cursor.getInt(13)==1);
		vcard.setUri(cursor.getString(14));
		return vcard;
	}
}
