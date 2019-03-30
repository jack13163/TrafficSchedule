package com.huawei.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.huawei.models.Car;
import com.huawei.models.Cross;
import com.huawei.models.Road;

public class BeanUtils {
	/**
	 */
	public List<Car> readFileGetCarBean(String pathname) {
		List<Car> objList = new ArrayList<Car>();
		try (FileReader reader = new FileReader(pathname); BufferedReader br = new BufferedReader(reader)) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("#")) {
					String str = line.substring(1, line.length() - 1);
					String[] row = str.split(",");

					Car car = new Car();
					car.setId(Integer.parseInt(row[0].trim()));
					car.setSid(Integer.parseInt(row[1].trim()));
					car.setEid(Integer.parseInt(row[2].trim()));
					car.setSpeed(Integer.parseInt(row[3].trim()));
					car.setStime(Integer.parseInt(row[4].trim()));
					objList.add(car);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return objList;
	}

	/**
	 */
	public List<Cross> readFileGetCrossBean(String pathname) {
		List<Cross> objList = new ArrayList<Cross>();
		try (FileReader reader = new FileReader(pathname); BufferedReader br = new BufferedReader(reader)) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("#")) {
					String str = line.substring(1, line.length() - 1);
					String[] row = str.split(",");

					Cross cross = new Cross();
					cross.setId(Integer.parseInt(row[0].trim()));
					cross.setUid(Integer.parseInt(row[1].trim()));
					cross.setRid(Integer.parseInt(row[2].trim()));
					cross.setDid(Integer.parseInt(row[3].trim()));
					cross.setLid(Integer.parseInt(row[4].trim()));
					objList.add(cross);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objList;
	}

	/**
	 */
	public List<Road> readFileGetRoadBean(String pathname) {
		List<Road> objList = new ArrayList<Road>();
		try (FileReader reader = new FileReader(pathname); BufferedReader br = new BufferedReader(reader)) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("#")) {
					String str = line.substring(1, line.length() - 1);
					String[] row = str.split(",");

					Road road = new Road();
					road.setId(Integer.parseInt(row[0].trim()));
					road.setLen(Integer.parseInt(row[1].trim()));
					road.setSpeed(Integer.parseInt(row[2].trim()));
					road.setChanel(Integer.parseInt(row[3].trim()));
					road.setSid(Integer.parseInt(row[4].trim()));
					road.setEid(Integer.parseInt(row[5].trim()));
					road.setDuplex(Integer.parseInt(row[6].trim()));
					objList.add(road);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objList;
	}
}
