package com.sunmiinfraredscanner;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.sunmi.scanner.IScanInterface;
import android.content.Intent;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.content.IntentFilter;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;

@ReactModule(name = SunmiInfraredScannerModule.NAME)
public class SunmiInfraredScannerModule extends ReactContextBaseJavaModule {
  public static final String NAME = "SunmiInfraredScanner";
  private static IScanInterface scanInterface;
  private Promise promise;
  private Intent serviceIntent;
  public static final String ACTION_DATA_CODE_RECEIVED = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED";
  private static final String DATA = "data";

  
  public SunmiInfraredScannerModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }


  private ServiceConnection conn = new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName componentName, IBinder service) {
          scanInterface = IScanInterface.Stub.asInterface(service);
      }

      @Override
      public void onServiceDisconnected(ComponentName componentName) {
          scanInterface = null;
      }
  };

    @ReactMethod
  public void openInfraredScanner(final Promise p) {
      promise = p;
      Activity currentActivity = getCurrentActivity();
      if (currentActivity == null) {
          promise.reject("E_ACTIVITY_DOES_NOT_EXIST", "Activity doesn't exist");
          return;
      }

      // final Intent intent = new Intent("com.summi.scan");
      // intent.setPackage("com.sunmi.sunmiqrcodescanner");

      serviceIntent = new Intent();
      serviceIntent.setPackage("com.sunmi.scanner");
      serviceIntent.setAction("com.sunmi.scanner.IScanInterface");
      currentActivity.bindService(serviceIntent, conn, Service.BIND_AUTO_CREATE);

      IntentFilter filter = new IntentFilter();
      filter.addAction(ACTION_DATA_CODE_RECEIVED);
      currentActivity.registerReceiver(receiver, filter);
      // System.out.println("scaaaaaaaaaaaaaaaaaaaaaaaaaaner"+ mCode);
      try {
              scanInterface.scan();
          
      } catch (Exception e) {
          e.printStackTrace();
      }

  }

  private BroadcastReceiver receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, final Intent intent) {
              System.out.println("Welcome");
              String code = intent.getStringExtra(DATA);
              System.out.println("cooooooooooooooooode"+code);
              if (code != null && !code.isEmpty()) {
                  promise.resolve(code);
              }            
          }
  };
}
