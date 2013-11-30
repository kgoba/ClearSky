package com.kosmoss.clearsky;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class LocalService extends Service {
	  private final IBinder mBinder = new MyBinder();
	  private ArrayList<String> list = new ArrayList<String>();

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {

	    Random random = new Random();
	    if (random.nextBoolean()) {
	      list.add("Linux");
	    }
	    if (random.nextBoolean()) {
	      list.add("Android");
	    }
	    if (random.nextBoolean()) {
	      list.add("iPhone");
	    }
	    if (random.nextBoolean()) {
	      list.add("Windows7");
	    }
	    if (list.size() >= 20) {
	      list.remove(0);
	    }
	    return Service.START_NOT_STICKY;
	  }
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	  public class MyBinder extends Binder {
		    LocalService getService() {
		      return LocalService.this;
		    }
		  }
	  
	  public List<String> getWordList() {
		    return list;
		  }
}
