package net.brennantaylor.gobbler;

import android.app.Application;

public class GobblerApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Redirectors.instance().init();
	}
}
