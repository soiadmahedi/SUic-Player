package com.soiadmahedi.suicTh;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

public class SoiadMahediApplication extends Application {
	
	private static Context mApplicationContext;
	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
	
	public static Context getContext() {
		return mApplicationContext;
	}
	
	/*
	
	SUic Player Developed by Md. Mehedi Hassan Rabbani and his 
    Brand Name is Soiad Mahedi. Bangladeshi Software Developer 
    and Digital Content Creator.
	
	Facebook : https://facebook.com/soiadmahediofficial
	Twitter : https://twitter.com/soiadmahedi
	Instagram : https://instagram.com/soiadmahedi
	Soiad Mahedi official blogspot site : 
	https://soiadmahedi.blogspot.com
	
	If you are facing "package file is invalid" error, 
    please install it again from product home page 
    (https://sites.google.com/view/suicplayer/download)
    
	If you have any question, Please contact us from my 
    Gmail address (mahedisbusiness@gmail.com)
	
	*/
	
	@Override
	public void onCreate() {
		mApplicationContext = getApplicationContext();
		this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		
		Thread.setDefaultUncaughtExceptionHandler(
		new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				Intent intent = new Intent(getApplicationContext(), DebugActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				intent.putExtra("error", Log.getStackTraceString(throwable));
				
				PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 11111, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
				
				AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, pendingIntent);
				
                SoiadMahediLogger.broadcastLog(Log.getStackTraceString(throwable));
				Process.killProcess(Process.myPid());
				System.exit(1);
				
				uncaughtExceptionHandler.uncaughtException(thread, throwable);
			}
		});
		SoiadMahediLogger.startLogging();
		super.onCreate();
	}
}
