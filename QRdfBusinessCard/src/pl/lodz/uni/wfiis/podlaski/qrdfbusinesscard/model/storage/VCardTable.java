package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.storage;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class VCardTable {

	//Database Table Columns
	public static final String VCARD_TABLE = "vcard";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NIP ="nip";
	public static final String COLUMN_REGON ="regon";
	public static final String COLUMN_BANK_NAME = "bank_name";
	public static final String COLUMN_ACCOUNT_NUMBER = "account_number";
	public static final String COLUMN_MAILTO = "mailto";
	public static final String COLUMN_STREET = "street";
	public static final String COLUMN_FLAT_NUMBER = "flat_number";
	public static final String COLUMN_HOUSE_NUMBER = "house_number";
	public static final String COLUMN_LOCALITY = "locality";
	public static final String COLUMN_POSTAL_CODE ="postal_code";
	public static final String COLUMN_POST_OFFICE ="post_office";
	public static final String COLUMN_IN_CONTACTS = "in_contacts";
	public static final String COLUMN_URI = "uri";
	public static final String[] allColumns = {
					COLUMN_ID , 
					COLUMN_NAME, COLUMN_NIP, COLUMN_REGON, 
					COLUMN_BANK_NAME, COLUMN_ACCOUNT_NUMBER, 
					COLUMN_MAILTO, 
					COLUMN_STREET, COLUMN_FLAT_NUMBER, COLUMN_HOUSE_NUMBER, 
					COLUMN_LOCALITY, COLUMN_POSTAL_CODE, COLUMN_POST_OFFICE, 
					COLUMN_IN_CONTACTS, COLUMN_URI};
	
	
	public static void createTable(SQLiteDatabase db)
	{
		String querry = "create table " + VCARD_TABLE + "("+
					COLUMN_ID + " integer primary key autoincrement, "+
					COLUMN_NAME + " text not null, "+
					COLUMN_NIP + " text, "+
					COLUMN_REGON +" text, "+
					COLUMN_BANK_NAME +" text, "+
					COLUMN_ACCOUNT_NUMBER +" text, "+
					COLUMN_MAILTO +" text, "+
					COLUMN_STREET +" text, "+
					COLUMN_FLAT_NUMBER +" integer, "+
					COLUMN_HOUSE_NUMBER +" integer, "+
					COLUMN_LOCALITY +" text, "+
					COLUMN_POSTAL_CODE +" text, "+
					COLUMN_POST_OFFICE +" text, "+
					COLUMN_IN_CONTACTS +" integer, "+
					COLUMN_URI +" text "+
					");";			
		db.execSQL(querry);
	}
	
	public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion)
	{
			Log.w(VCardTable.class.getName(), "Upgrading database from version "
		        + oldVersion + " to " + newVersion
		        + ", which will destroy all old data");
		    db.execSQL("DROP TABLE IF EXISTS " + VCARD_TABLE);
		    createTable(db);
	}
}
