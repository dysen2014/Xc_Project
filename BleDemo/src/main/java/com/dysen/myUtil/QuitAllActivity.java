package com.dysen.myUtil;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;

public class QuitAllActivity extends Application {
	private List<Activity> mList = new LinkedList<Activity>();
	private static QuitAllActivity instance;

	private QuitAllActivity() {
	}

	public synchronized static QuitAllActivity getInstance() {
		if (null == instance) {
			instance = new QuitAllActivity();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
}
