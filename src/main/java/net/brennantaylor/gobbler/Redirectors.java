package net.brennantaylor.gobbler;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

import com.squareup.okhttp.OkHttpClient;

public class Redirectors {
	private static Redirectors sRedirectors = new Redirectors();

	private OkHttpClient client;
	private List<RedirectHandler> handlers;

	public static Redirectors instance() {
		return sRedirectors;
	}

	private Redirectors() {
		client = new OkHttpClient();
		client.setFollowProtocolRedirects(false);

		handlers = new ArrayList<RedirectHandler>();
	}

	public static void init() {
		instance().handlers.add(new FacebookHandler());
		instance().handlers.add(new TwitterHandler());
	}

	public static OkHttpClient getClient() {
		return instance().client;
	}

	public static RedirectHandler getHandler(Uri uri) {
		for (RedirectHandler handler : instance().handlers) {
			if (handler.shouldHandle(uri)) {
				return handler;
			}
		}

		return null;
	}
}
