package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard;

import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.storage.VCardDataSource;
import android.app.Application;

public class QRdfBusinessCard extends Application {
	private static QRdfBusinessCard _instance;
	private VCardDataSource _localStorage;

	@Override
	public void onCreate()
	{
	    super.onCreate();
	    _instance = this;
	}

	

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		_localStorage.close();
		super.onTerminate();
	}



	public static QRdfBusinessCard getInstance()
	{
	        //Exposes a mechanism to get an instance of the 
	        //custom application object.
	        return _instance;
	}

	public synchronized VCardDataSource getDatabase()
	{
	    if (_localStorage == null)
	    {
	        _localStorage = new VCardDataSource(this);
	    }

	    return _localStorage;
	}
	
}
