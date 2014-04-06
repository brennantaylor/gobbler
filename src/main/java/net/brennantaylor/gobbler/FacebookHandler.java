package net.brennantaylor.gobbler;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class FacebookHandler implements RedirectHandler {
	private static final String TAG = "GobblerFacebook";

	@Override
	public boolean shouldHandle(Uri incoming) {
		return "m.facebook.com".equals(incoming.getHost());
	}

	@Override
	public RedirectResult handle(Uri incoming) {
		RedirectResult result = new RedirectResult();
		result.incoming = incoming;
		result.outgoing = null;

		try {
			OkHttpClient client = Redirectors.instance().getClient();
			Request.Builder builder = new Request.Builder().url(incoming.toString());

			Request request = builder.build();
			Response response = client.execute(request);

			StringBuilder info = new StringBuilder();
			info.append(response.statusLine());
			info.append("\n");
			info.append(response.headers().toString());
			info.append("\n");
			info.append(response.body().string());
			info.append("\n");
			Log.v(TAG, info.toString());
			response.body().close();

			String refresh = response.header("Refresh");
			if (!TextUtils.isEmpty(refresh)) {
				String[] splits = refresh.split("URL=");
				result.outgoing = Uri.parse(splits[splits.length-1]);
			}
		} catch (IOException e) {
			Log.e(TAG, "Could not handle uri=:" + incoming, e);
		}

		return result;
	}
}
