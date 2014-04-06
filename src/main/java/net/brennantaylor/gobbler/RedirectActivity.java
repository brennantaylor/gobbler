package net.brennantaylor.gobbler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RedirectActivity extends Activity {
	private final String TAG = "Gobbler";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		final Uri incoming = intent.getData();
		Log.d(TAG, "onCreate() incoming=" + incoming);

		RedirectTask redirect = new RedirectTask();
		redirect.execute(incoming);

		finish();
	}

	private class RedirectTask extends AsyncTask<Uri, Integer, List<RedirectResult>> {
		@Override
		protected List<RedirectResult> doInBackground(Uri... uris) {
			int count = uris.length;
			List<RedirectResult> results = new ArrayList<RedirectResult>();

			for (int i = 0; i < count; i++) {
				final Uri uri = uris[i];
				RedirectHandler handler = Redirectors.getHandler(uri);
				if (handler == null) {
					Log.e(TAG, "Could not handle uri=" + uri);
					continue;
				}

				RedirectResult result = handler.handle(uri);
				results.add(result);
				publishProgress((int) ((i / (float) count) * 100));

				if (isCancelled()) {
					break;
				}
			}
			return results;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// ignore
		}

		@Override
		protected void onPostExecute(List<RedirectResult> results) {
			for (RedirectResult result : results) {
				Log.d(TAG, "incoming=" + result.incoming + " mapped to outgoing=" + result.outgoing);
				if (result.outgoing != null) {
					Intent intent = new Intent(Intent.ACTION_VIEW, result.outgoing);
					startActivity(intent);
				}
			}
		}
	}
}
