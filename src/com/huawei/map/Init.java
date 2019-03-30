package com.huawei.map;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.huawei.Main;
import com.huawei.models.Answer;
import com.huawei.models.Car;
import com.huawei.models.CarCompare;
import com.huawei.models.Road;
import com.huawei.utils.BeanUtils;

public class Init {
	private BeanUtils beanUtils = null;
	private List<Car> carList = null;
	private Map<Integer, Car> carsHashMap = null;
	private int TotalCars = 0;
	private int ScheduledCars = 0;
	private List<Answer> answers = null;
	// number of car on each road and each direction
	private Map<Integer, int[]> carsEachRoad = null;
	private Map<Integer, Queue<Car>> carPort = null;
	private List<Car> carsOnRoad = null;
	// weight of road(both direction)
	public static Map<Integer, double[]> c = null;

	public List<Car> getCarsOnRoad() {
		return carsOnRoad;
	}

	public Map<Integer, Queue<Car>> getCarPort() {
		return carPort;
	}

	public Map<Integer, Car> getCarsHashMap() {
		return carsHashMap;
	}

	public int getTotalCars() {
		return TotalCars;
	}

	public int getScheduledCars() {
		return ScheduledCars;
	}

	public void setScheduledCars(int scheduledCars) {
		ScheduledCars = scheduledCars;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public Map<Integer, int[]> getCarsEachRoad() {
		return carsEachRoad;
	}

	public static Map<Integer, double[]> getC() {
		return c;
	}

	/**
	 */
	private static Init instance = null;

	public static Init getInstance() {
		if (instance == null) {
			instance = new Init();
		}
		return instance;
	}

	public void release() {
		beanUtils = null;
		carsOnRoad = null;
		carPort = null;
		carsHashMap = null;
		answers = null;
		carsEachRoad = null;
		c = null;
		instance = null;
	}

	/**
	 */
	private Init() {
		beanUtils = new BeanUtils();
		carsOnRoad = new LinkedList<Car>();
		carPort = new HashMap<Integer, Queue<Car>>();
		carsHashMap = new HashMap<Integer, Car>();
		answers = new LinkedList<Answer>();
		carsEachRoad = new HashMap<Integer, int[]>();
		c = new HashMap<Integer, double[]>();

		carList = beanUtils.readFileGetCarBean(Main.carfile);
		TotalCars = carList.size();

		for (Car car : carList) {
			int SId = car.getSid();
			Queue<Car> port = null;
			if (!carPort.containsKey(SId)) {
				port = new LinkedList<Car>();
				carPort.put(SId, port);
			} else {
				port = carPort.get(SId);
			}
			port.add(car);
			carsHashMap.put(car.getId(), car);
		}

		for (Integer crossId : carPort.keySet()) {
			((LinkedList<Car>) carPort.get(crossId)).sort(new CarCompare());
		}

		// add
		for (Road road : MapHelper.getInstance().getRoadList()) {
			if (road.getDuplex() == 0) {
				carsEachRoad.put(road.getId(), new int[1]);
			} else {
				carsEachRoad.put(road.getId(), new int[2]);
			}
			double[] weight = new double[2];
			weight[0] = 1;
			weight[1] = 1;
			c.put(road.getId(), weight);
		}
	}
}
