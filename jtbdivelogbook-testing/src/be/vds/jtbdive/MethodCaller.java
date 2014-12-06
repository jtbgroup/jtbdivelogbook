package be.vds.jtbdive;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodCaller {
public static void main(String[] args) {
	MyClass m = new MyClass();
	try {
		Method method = m.getClass().getMethod("getString", null);
		System.out.println(method.invoke(m, null));
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchMethodException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}

class MyClass{
	public String getString(){
		return "Hello World!";
	}
}
