package net.brennantaylor.gobbler;

import android.net.Uri;

public interface RedirectHandler {
	public boolean shouldHandle(Uri incoming);
	public RedirectResult handle(Uri incoming);
}
