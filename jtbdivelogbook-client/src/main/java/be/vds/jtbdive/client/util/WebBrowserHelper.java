/*
* Jt'B Dive Logbook - Electronic dive logbook.
* 
* Copyright (C) 2010  Gautier Vanderslyen
* 
* Jt'B Dive Logbook is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package be.vds.jtbdive.client.util;

import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author gautier
 */
public class WebBrowserHelper {

	public static  void browse(String url) throws IOException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InterruptedException {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					desktop.browse(new java.net.URI(url));
					return;
				} catch (java.io.IOException e) {
					e.printStackTrace();
				} catch (java.net.URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}

		String osName = System.getProperty("os.name");
			if (osName.startsWith("Windows")) {
				Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler " + url);
			} else if (osName.startsWith("Mac OS")) {
				Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
				java.lang.reflect.Method openURL = fileMgr.getDeclaredMethod(
						"openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else {
				// check for $BROWSER
				java.util.Map<String, String> env = System.getenv();
				if (env.get("BROWSER") != null) {
					Runtime.getRuntime().exec(env.get("BROWSER") + " " + url);
					return;
				}

				// check for common browsers
				String[] browsers = { "firefox", "iceweasel", "chrome",
						"opera", "konqueror", "epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime()
							.exec(new String[] { "which", browsers[count] })
							.waitFor() == 0) {
						browser = browsers[count];
						break;
					}
				if (browser == null)
					throw new RuntimeException("couldn't find any browser...");
				else
					Runtime.getRuntime().exec(new String[] { browser, url });
			}
	}
}
