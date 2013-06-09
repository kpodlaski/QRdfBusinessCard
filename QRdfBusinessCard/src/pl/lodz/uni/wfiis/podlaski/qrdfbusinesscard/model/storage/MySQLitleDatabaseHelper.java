package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLitleDatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "B2BO_VCard.db";
    
    MySQLitleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		VCardTable.createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		VCardTable.updateTable(db, oldVersion, newVersion);
	}

}
