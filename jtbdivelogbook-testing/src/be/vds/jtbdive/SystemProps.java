package be.vds.jtbdive;

import java.util.Iterator;
import java.util.Properties;

public class SystemProps {
	public static void main(String[] args) {
		Properties props = System.getProperties();
		for (Iterator<Object> iterator = props.keySet().iterator(); iterator.hasNext();) {
			Object key = (Object) iterator.next();
			System.out.println(key.toString() + " = " + props.getProperty((String)key));
		}
	}
}
