/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 * 
 * Created by lisah0 on 2012-02-24
 */
package pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.gui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R.id;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.R.layout;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.model.VCard;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.rdf.JobWorker;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.rdf.RDFVCardReader;
import pl.lodz.uni.wfiis.podlaski.qrdfbusinesscard.rdf.ResultsListener;
import net.sourceforge.zbar.android.cameratest.CameraPreview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Button;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

import android.widget.TextView;
import android.graphics.ImageFormat;

/* Import ZBar Class files */
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

public class CameraTestActivity extends Activity implements ResultsListener
{
    private Camera mCamera;
    private net.sourceforge.zbar.android.cameratest.CameraPreview mPreview;
    private Handler autoFocusHandler;
    private RDFVCardReader vCardReader;
    TextView scanText;
    Button scanButton;

    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    } 

    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_qrcode);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

                
        
        mPreview = new net.sourceforge.zbar.android.cameratest.CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreviewL);
        preview.addView(mPreview);

        scanText = (TextView)findViewById(R.id.scanText);
        //TODO cut hard codded default url
        scanText.setText("http://merlin.phys.uni.lodz.pl/KPodlaski/test2/firma2.ttl");
        scanButton = (Button)findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (barcodeScanned) {
                        barcodeScanned = false;
                        scanText.setText("Scanning...");
                        mCamera.setPreviewCallback(previewCb);
                        mCamera.startPreview();
                        previewing = true;
                        mCamera.autoFocus(autoFocusCB);
                    }
                }
            });
        
        Button detailsButton = (Button) findViewById(R.id.DetailsButton);
        detailsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (scanText.getText().length()>1)
				{
					try {
						vCardReader = new RDFVCardReader(scanText.getText().toString(), CameraTestActivity.this);
						vCardReader.start();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
        
        
    }

   
    
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
            public void run() {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            }
        };

    PreviewCallback previewCb = new PreviewCallback() {
            public void onPreviewFrame(byte[] data, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                Size size = parameters.getPreviewSize();

                Image barcode = new Image(size.width, size.height, "Y800");
                barcode.setData(data);

                int result = scanner.scanImage(barcode);
                
                if (result != 0) {
                    previewing = false;
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    
                    SymbolSet syms = scanner.getResults();
                    for (Symbol sym : syms) {
                        scanText.setText(sym.getData());
                        barcodeScanned = true;
                    }
                }
            }
        };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                autoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        };

	@Override
	public void jobFinished(JobWorker worker) {
		Object result = (VCard) worker.getJobResult();
		if (!(result instanceof VCard)) return;
		VCard vcard = (VCard) result;
		Intent i = new Intent(getApplicationContext(), CompanyDetailsActivity.class);
		i.putExtra("vcard",vcard);
		startActivity(i);			
	}
}
