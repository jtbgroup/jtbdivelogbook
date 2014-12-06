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
package be.vds.jtbdive.core.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;



/**
 * This appenders is based on Log4j framework. It keeps a list of
 * {@link LoggingListener} and notify them when an event is logged by passing
 * the event formatted with the defined formatter.
 * <p>
 * It has a buffer of String that caches all the messages once the listener is
 * activated. The size of the buffer is by default 20.
 * <p>
 * Actually there is no way to change the cache size, but it could be done
 * easilly.
 * <p>
 * The possibility exist to (des)activate the caching. By default, it is
 * enabled, but a constructor can be used to disable it. A setter can be created
 * to (des)activate it at run time
 * 
 * @author Gautier Vanderslyen
 * 
 */
public class ListenerAppender extends WriterAppender{

	private LinkedBlockingQueue<LoggingEvent> cache;
	private List<LoggingListener> listeners = new ArrayList<LoggingListener>();
	private boolean cacheActivated;

	public ListenerAppender() {
		this(true);
	}

	public ListenerAppender(boolean cacheActivated) {
		this(cacheActivated, 20);
	}

	public ListenerAppender(int cacheSize) {
		this(true, cacheSize);
	}


	public ListenerAppender(boolean cacheActivated, int cacheSize) {
		this.cacheActivated = cacheActivated;
		createCache(cacheSize);
	}

	private void createCache(int cacheSize) {
		cache = new LinkedBlockingQueue<LoggingEvent>(cacheSize);
	}

	public void addLoggerListener(LoggingListener listener) {
		listeners.add(listener);
	}


	public void append(LoggingEvent event) {
		if (cacheActivated) {
			if (cache.remainingCapacity() <= 0) {
				cache.poll();
			}
			cache.offer(event);
		}

		for (LoggingListener listener : listeners) {
			listener.log(event);
		}
	}
	
	public Queue<LoggingEvent> getCache() {
		return cache;
	}

}
