package com.huawei.models;

import java.util.Comparator;

public class CarCompare implements Comparator<Car> {

	@Override
	public int compare(Car o1, Car o2) {
		return Integer.compare(o1.getStime(), o2.getStime()) != 0 ? Integer.compare(o1.getStime(), o2.getStime())
				: Integer.compare(o1.getId(), o2.getId());
	}

}
