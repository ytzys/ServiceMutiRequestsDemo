package com.ytzys.servicemutirequestsdemo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author ytzys
 * 
 *         ��ͬʱ���ն�����󣬵�ֻ��finish()�����ж����һ����������β������
 *         Ӧ�ó������磺����ӽ�Ŀ����Ҫ���ƽ��ٲ��ţ�����ε��ʱ��ֻ�������һ�ε���Ľ�Ŀ
 * 
 */
public class MutiRequestsService extends Service {

	private int programId;
	private int subId; // ��ǵ�ǰΪ�ĸ��ӽ�Ŀ���񣬶������ʱ��¼���һ�ε��õ��Խ�Ŀid
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
			Log.i(TAG, "START_SERVICE");
			programId = intent.getIntExtra("programId", 0);
			subId = intent.getIntExtra("subId", 0);

			// ���д����߳�
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

	// �������ʱ���ã�����β�������粥����Ӧ���ӽ�Ŀ
	private void finish(int id) {

		// �����һ��������ӽ�Ŀid��ͬ
		if (subId == id) {
			Log.i(TAG, "finish(), subId:" + subId);
			Intent intent = new Intent("finish");
			intent.putExtra("subId", subId);
			sendBroadcast(intent);
		}
	}

	// �����̣߳��ṩ����ķ���
	class HndleThread extends Thread {
		MutiRequestsService service;
		int subId;

		HndleThread(MutiRequestsService service, int id) {
			this.service = service;
			this.subId = id;
		}

		public void run() {
			try {
				sleep(3000);
				/**
				 * handling process
				 * */
				service.finish(subId);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
