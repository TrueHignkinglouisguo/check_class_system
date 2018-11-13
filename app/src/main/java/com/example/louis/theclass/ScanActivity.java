package com.example.louis.theclass;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
public class ScanActivity extends Activity implements DecoratedBarcodeView.TorchListener {
    private ImageView flashlight,back;
    private DecoratedBarcodeView decoratedBarcodeView;
    private CaptureManager captureManager;
    private boolean isLightOn = false;
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return decoratedBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = this.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.scanpage);
        flashlight= (ImageView) findViewById(R.id.scanpage_flashlight);
        back = (ImageView) findViewById(R.id.scanpage_back);
        decoratedBarcodeView= (DecoratedBarcodeView) findViewById(R.id.scanpage_scan);
        decoratedBarcodeView.setTorchListener(this);
        // 如果没有闪光灯功能，就去掉相关按钮
        if (!hasFlash()) {
            flashlight.setVisibility(View.GONE);
        }
        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, decoratedBarcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();
        flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightOn) {
                    decoratedBarcodeView.setTorchOff();
                } else {
                    decoratedBarcodeView.setTorchOn();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void onTorchOn() {
        isLightOn = true;
    }
    public void onTorchOff() {
        isLightOn = false;
    }
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}