package com.huawei.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.huawei.Main;
import com.huawei.dynamicprogramming.DynamicProgramming;
import com.huawei.models.Car;
import com.huawei.models.Cross;
import com.huawei.models.Road;
import com.huawei.utils.BeanUtils;
import com.huawei.utils.Helper;

public class MapHelper {
	private BeanUtils beanUtils = new BeanUtils();

	private List<Road> roadList = null;
	private List<Cross> crossList = null;
	private HashMap<Integer, Road> roadHashMap = null;
	private HashMap<Integer, Cross> crossHashMap = null;
	private HashMap<Integer, int[][][]> roadStatus = null;
	private Map<Integer, Integer> idToNo = null;// cross id to cross no.
	private Map<Integer, Integer> noToId = null;// cross no to cross id.

	public Map<Integer, Integer> getNoToId() {
		return noToId;
	}

	public List<Road> getRoadList() {
		return roadList;
	}

	public List<Cross> getCrossList() {
		return crossList;
	}

	public HashMap<Integer, Road> getRoadHashMap() {
		return roadHashMap;
	}

	public HashMap<Integer, Cross> getCrossHashMap() {
		return crossHashMap;
	}

	public HashMap<Integer, int[][][]> getRoadStatus() {
		return roadStatus;
	}

	public Map<Integer, Integer> getIdToNo() {
		return idToNo;
	}

	/**
	 */
	private static MapHelper instance = null;

	private MapHelper() {
		roadHashMap = new HashMap<Integer, Road>();
		crossHashMap = new HashMap<Integer, Cross>();
		roadStatus = new HashMap<Integer, int[][][]>();
		idToNo = new HashMap<Integer, Integer>();
		noToId = new HashMap<Integer, Integer>();

		roadList = beanUtils.readFileGetRoadBean(Main.roadfile);
		crossList = beanUtils.readFileGetCrossBean(Main.crossfile);

		for (int i = 0; i < roadList.size(); i++) {
			Road road = roadList.get(i);
			int rId = road.getId();
			int channel = road.getChanel();
			int len = road.getLen();

			int[][][] status = null;
			if (road.getDuplex() == 0) {
				status = new int[1][channel][len];
			} else {
				status = new int[2][channel][len];
			}

			roadStatus.put(rId, status);
			roadHashMap.put(rId, road);
		}

		for (int i = 0; i < crossList.size(); i++) {
			Cross cross = crossList.get(i);
			crossHashMap.put(cross.getId(), cross);

			int crossId = cross.getId();
			if (!idToNo.containsKey(crossId)) {
				idToNo.put(crossId, i);
				noToId.put(i, crossId);
			} else {
				System.out.println("出错啦!");
			}
		}
	}

	public void release() {
		roadHashMap = null;
		crossHashMap = null;
		roadStatus = null;
		roadList = null;
		crossList = null;
		idToNo = null;
		noToId = null;
		instance = null;
	}

	public static MapHelper getInstance() {
		if (instance == null) {
			instance = new MapHelper();
		}
		return instance;
	}

	/**
	 * shortest path from startIndex to destIndex. begins at 0
	 * 
	 * @param startIndex
	 * @param destIndex
	 * @return
	 */
	public Queue<Road> findPath(int startIndex, int destIndex) {
		Queue<Road> path = DynamicProgramming.getPath(startIndex, destIndex);
		return path;
	}

	/**
	 * shortest path from startIndex to destIndex. begins at 0
	 * 
	 * @param startIndex
	 * @param destIndex
	 * @param num
	 * @return
	 */
	public List<Queue<Road>> findPaths(int startIndex, int destIndex, int num) {
		List<Queue<Road>> paths = new LinkedList<Queue<Road>>();
		int p = MapHelper.getInstance().getCrossList().size();
		int[] r = Helper.randInt(0, p, num);

		for (int i = 0; i < num; i++) {
			Queue<Road> path1 = DynamicProgramming.getPath(startIndex, r[i]);
			Queue<Road> path2 = DynamicProgramming.getPath(r[i], destIndex);
			if (path1 != null && path2 != null) {
				for (Road road : path2) {
					path1.add(road);
				}
				paths.add(path1);
			}
		}

		return paths;
	}

	/**
	 * 
	 * @param road
	 * @param car
	 * @return
	 */
	public int getRoadChannel(int[][] roadStatus) {
		for (int i = 0; i < roadStatus.length; i++) {
			if (roadStatus[i][0] == 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @param car
	 * @return
	 */
	public int getNextCrossId(Car car) {
		return car.getPosition_road().getSid() == car.getPosition_cross() ? car.getPosition_road().getEid()
				: car.getPosition_road().getSid();
	}

	/**
	 * 
	 * @param cross1
	 * @param cross2
	 * @return
	 */
	public Road getRoadByCrosses(int cross1, int cross2) {
		List<Integer> roads = new ArrayList<Integer>();
		int count = 0;
		for (Cross c : MapHelper.getInstance().getCrossList()) {
			if (c.getId() == cross1 || c.getId() == cross2) {
				roads.add(c.getUid());
				roads.add(c.getRid());
				roads.add(c.getDid());
				roads.add(c.getLid());
				count++;
			}
			if (count >= 2) {
				break;
			}
		}
		Integer[] array = new Integer[roads.size()];
		try {
			Road road = MapHelper.getInstance().getRoadHashMap().get(Helper.findDupicateInArray(roads.toArray(array)));
			return road;
		} catch (Exception e) {
			return null;
		}
	}
}
