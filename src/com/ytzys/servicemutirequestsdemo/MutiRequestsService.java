package com.ytzys.servicemutirequestsdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author ytzys
 * 
 *         可同时接收多个请求，但只在finish()方法中对最后一次请求做收尾工作。
 *         应用场景比如：多个子节目都需要现破解再播放，当多次点击时，只播放最后一次点击的节目
 * 
 */
public class MutiRequestsService extends Service {

	private int programId;
	private int subId; // 标记当前为哪个子节目服务，多次请求时记录最后一次调用的自节目id
	public static final int START_SERVICE = 1;
	public static final int STOP_SERVICE = 2;
	private static final String TAG = "MutiRequstsService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int action = intent.getIntExtra("action", 0);
		switch (action) {
		case START_SERVICE:
			programId = intent.getIntExtra("programId", 0);
			subId = intent.getIntExtra("subId", 0);
			Log.i(TAG, "START_SERVICE, programId:" + programId + ", subId:"
					+ subId);

			// 运行处理线程
			new HndleThread(this, subId).start();
			break;
		case STOP_SERVICE:
			Log.i(TAG, "STOP_SERVICE, programId:" + programId);
			stopSelf(startId);
			break;
		default:
			break;
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// 处理结束时调用，做收尾工作，如播放相应的子节目
	private void finish(int id) {

		// 判断是否与最后一次请求的子节目id相同
		if (subId == id) {
			Log.i(TAG, "finish(), subId:" + subId);
			Intent intent = new Intent("finish");
			intent.putExtra("subId", subId);
			sendBroadcast(intent);
		}
	}

	// 处理线程，提供具体的服务
	class HndleThread extends Thread {
		MutiRequestsService service;
		int subId;

		HndleThread(MutiRequestsService service, int id) {
			this.service = service;
			this.subId = id;
		}

		public void run() {
			try {
				/**
				 * handling process
				 * */
				sleep(3000);

				// 处理结束后回调
				service.finish(subId);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
