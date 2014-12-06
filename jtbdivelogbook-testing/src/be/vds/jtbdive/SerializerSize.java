package be.vds.jtbdive;

import java.io.Serializable;

import be.vds.jtbdive.core.utils.ObjectSerializer;

public class SerializerSize implements Serializable {

	private static final long serialVersionUID = 1465845308308583202L;

	public static void main(String[] args) {
		new SerializerSize().startTest();
	}

	private void startTest() {
		System.out.println("TEST 1");
		testOne();
		System.out.println("============================");
		System.out.println("TEST 2");
		testTwo();
		System.out.println("============================");
		
	}

	private void testTwo() {
		Vis vis = new Vis();
		CarExt[] cars = new CarExt[100000];
		for (int i = 0; i < cars.length; i++) {
			cars[i] = new CarExt("Car " + i, vis);
		}
		Garage garage = new Garage(cars, vis);

		byte[] bytes = ObjectSerializer.serialize(vis);
		System.out.println("Vis is " + bytes.length);

		bytes = ObjectSerializer.serialize(garage);
		System.out.println("Garage is " + bytes.length);
	}

	
	private void testOne() {
		Vis vis = new Vis();
		Car[] cars = new Car[100000];
		for (int i = 0; i < cars.length; i++) {
			cars[i] = new CarExt("Car " + i, null);
		}
		Garage garage = new Garage(cars, vis);

		byte[] bytes = ObjectSerializer.serialize(vis);
		System.out.println("Vis is " + bytes.length);

		bytes = ObjectSerializer.serialize(garage);
		System.out.println("Garage is " + bytes.length);
	}

	private class Garage implements Serializable {
		private static final long serialVersionUID = -5263437646193328853L;
		private Car[] cars;
		private Vis vis;

		public Garage(Car[] cars, Vis vis) {
			this.cars = cars;
			this.vis = vis;
		}

	};

	private class Vis implements Serializable {
		private static final long serialVersionUID = 8436987017672668605L;
		private String[] vis;

		public Vis() {
			initList();
		}

		private void initList() {
			vis = new String[100000];
			for (int i = 0; i < vis.length; i++) {
				vis[i] = String.valueOf(i);
			}
		}
	};

	private class Car implements Serializable {
		private static final long serialVersionUID = 9005891075073498777L;
		private String name;

		public Car(String name) {
			this.name = name;
		}
	};

	private class CarExt extends Car {

		private static final long serialVersionUID = -7574963355446143134L;
		private Vis vis;
		public CarExt(String name, Vis vis) {
			super(name);
			this.vis = vis;
		}
	}
}
