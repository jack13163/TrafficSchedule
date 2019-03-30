package com.huawei.models;

import java.util.Queue;

public class Answer {
	private int carId;
	private int sTime;
	private Queue<Integer> path;

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public int getsTime() {
		return sTime;
	}

	public void setsTime(int sTime) {
		this.sTime = sTime;
	}

	public Queue<Integer> getPath() {
		return path;
	}

	public void setPath(Queue<Integer> path) {
		this.path = path;
	}

	@Override
	public String toString() {
		String ret = "(" + carId + "," + sTime;

		for (Integer r : path) {
			ret += "," + r;
		}
		ret += ")";

		return ret;
	}
}
