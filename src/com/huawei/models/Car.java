package com.huawei.models;

import java.util.LinkedList;
import java.util.Queue;

import com.huawei.Main;
import com.huawei.map.Init;
import com.huawei.map.MapHelper;
import com.huawei.utils.Helper;

public class Car {
	private int id;
	private int sid;
	private int eid;
	private int speed;
	private int stime;

	private CarStatus status = CarStatus.Wait;
	private int position_cross;
	private Road position_road;
	private int position_channel;
	private Queue<Road> path;

	public Road getPosition_road() {
		return position_road;
	}

	public void setPosition_road(Road position_road) {
		this.position_road = position_road;
	}

	public int getPosition_cross() {
		return position_cross;
	}

	public void setPosition_cross(int position_cross) {
		this.position_cross = position_cross;
	}

	public int getPosition_channel() {
		return position_channel;
	}

	public void setPosition_channel(int position_channel) {
		this.position_channel = position_channel;
	}

	public Queue<Road> getPath() {
		return path;
	}

	public void setPath(Queue<Road> path) {
		this.path = path;
	}

	public CarStatus getStatus() {
		return status;
	}

	public void setStatus(CarStatus status) {
		this.status = status;
	}

	public Car() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getStime() {
		return stime;
	}

	public void setStime(int stime) {
		this.stime = stime;
	}

	@Override
	public String toString() {
		return "(" + id + "," + sid + "," + eid + "," + speed + "," + stime + ")";
	}

	public Car(int id, int sid, int eid, int speed, int stime) {
		super();
		this.id = id;
		this.sid = sid;
		this.eid = eid;
		this.speed = speed;
		this.stime = stime;
	}

	/**
	 * 
	 * @param car
	 * @return
	 */
	public CarDirection GetCarDirection() {
		int currentRoad = this.getPosition_road().getId();
		int nextRoad = this.getPath().peek().getId();

		int nextCrossId = MapHelper.getInstance().getNextCrossId(this);
		Cross nextCross = MapHelper.getInstance().getCrossHashMap().get(nextCrossId);

		if (nextCross.getUid() == currentRoad) {
			if (nextCross.getDid() == nextRoad) {
				return CarDirection.Forward;
			} else if (nextCross.getLid() == nextRoad) {
				return CarDirection.Right;
			} else if (nextCross.getRid() == nextRoad) {
				return CarDirection.Left;
			}
		} else if (nextCross.getDid() == currentRoad) {
			if (nextCross.getUid() == nextRoad) {
				return CarDirection.Forward;
			} else if (nextCross.getLid() == nextRoad) {
				return CarDirection.Left;
			} else if (nextCross.getRid() == nextRoad) {
				return CarDirection.Right;
			}
		} else if (nextCross.getLid() == currentRoad) {
			if (nextCross.getDid() == nextRoad) {
				return CarDirection.Right;
			} else if (nextCross.getUid() == nextRoad) {
				return CarDirection.Left;
			} else if (nextCross.getRid() == nextRoad) {
				return CarDirection.Forward;
			}
		} else if (nextCross.getRid() == currentRoad) {
			if (nextCross.getLid() == nextRoad) {
				return CarDirection.Forward;
			} else if (nextCross.getUid() == nextRoad) {
				return CarDirection.Right;
			} else if (nextCross.getDid() == nextRoad) {
				return CarDirection.Left;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param T
	 */
	public void start(int T) {
		Queue<Road> path = MapHelper.getInstance().findPath(this.getSid(), this.getEid());

		Road road = path.peek();

		int k = 0;
		if (road.getDuplex() == 1) {
			if (this.getSid() != road.getSid()) {
				k = 1;
			}
		}
		int[][] RoadStatus = MapHelper.getInstance().getRoadStatus().get(road.getId())[k];

		// decide whether to start
		int count = getTheNumberOfCars(RoadStatus);
		int total = road.getLen() * road.getChanel();
		double rate = 1.0 * count / total;
		int channel = MapHelper.getInstance().getRoadChannel(RoadStatus);

		// do operate by road status
		if (channel != -1 && rate <= Main.PassRate) {
			int L = Helper.findFirst(RoadStatus[channel]);

			if (L != 0) {
				int S1 = Math.min(this.getSpeed(), road.getSpeed());
				int S2 = 0;

				if (L > 0) {
					S2 = L;
				} else {
					S2 = road.getLen();
				}

				int S = Math.min(S1, S2) - 1;
				RoadStatus[channel][S] = this.getId();

				Queue<Integer> path_back = new LinkedList<Integer>();
				for (Road r : path) {
					path_back.add(r.getId());
				}

				this.setStime(T);
				path.remove();
				this.setPosition_road(road);
				this.setPath(path);
				this.setPosition_cross(this.getSid());
				this.setPosition_channel(channel);

				Init.getInstance().getCarsOnRoad().add(this);

				Answer answer = new Answer();
				answer.setCarId(this.getId());
				answer.setsTime(T);
				answer.setPath(path_back);
				Init.getInstance().getAnswers().add(answer);
			}
		}
	}

	/**
	 */
	public void run(int T) {
		int k = 0;
		if (this.getPosition_cross() != this.getPosition_road().getSid()) {
			k = 1;
		}
		int[][] RoadStatus = MapHelper.getInstance().getRoadStatus().get(this.getPosition_road().getId())[k];
		int P = Helper.findFirstEqual(RoadStatus[this.getPosition_channel()], this.getId());

		int S1 = Math.min(this.getSpeed(), this.getPosition_road().getSpeed());

		int L = Helper.findFirst(RoadStatus[this.getPosition_channel()], P + 1, 0);

		int S2 = 0;
		if (L != -1) {
			S2 = L - P - 1;
		} else {
			S2 = this.getPosition_road().getLen() - P - 1;
		}

		if (S1 > S2 && ((this.getPosition_road().getSid() == this.getEid())
				|| (this.getPosition_road().getEid() == this.getEid()))) {
			this.release(T);
		} else {

			int S = Math.min(S1, S2);

			RoadStatus[this.getPosition_channel()][P] = 0;
			RoadStatus[this.getPosition_channel()][P + S] = this.getId();
		}
		this.setStatus(CarStatus.Stop);
	}

	/**
	 * 
	 * @param channel
	 * @param S
	 */
	public void across(int channel, int S, int T) {
		int k = 0;
		if (this.getPosition_cross() == this.getPosition_road().getEid()) {
			k = 1;
		}
		int[][] RoadStatus = MapHelper.getInstance().getRoadStatus().get(this.getPosition_road().getId())[k];

		int Position = Helper.findFirstEqual(RoadStatus[this.getPosition_channel()], this.getId());
		RoadStatus[this.getPosition_channel()][Position] = 0;
		// int oldRoadId = this.getPosition_road().getId();

		int CrossId = MapHelper.getInstance().getNextCrossId(this);
		this.setPosition_cross(CrossId);

		if (!this.getPath().isEmpty()) {
			Road nextRoad = this.getPath().remove();
			this.setPosition_road(nextRoad);

			this.setPosition_channel(channel);

			int[][] NextRoadStatus = this.getPosition_cross() == nextRoad.getSid()
					? MapHelper.getInstance().getRoadStatus().get(nextRoad.getId())[0]
					: MapHelper.getInstance().getRoadStatus().get(nextRoad.getId())[1];

			int L = Helper.findFirst(NextRoadStatus[channel]);
			if (L < 0) {
				L = nextRoad.getLen();
			}

			int P = Math.min(S, L) - 1;
			NextRoadStatus[channel][P] = this.getId();
		} else {
			this.release(T);
		}
		this.setStatus(CarStatus.Stop);
	}

	/**
	 * 
	 * @param T
	 */
	public void release(int T) {
		int k = 0;
		if (this.getPosition_cross() != this.getPosition_road().getSid()) {
			k = 1;
		}
		int[][] RoadStatus = MapHelper.getInstance().getRoadStatus().get(this.getPosition_road().getId())[k];
		int P = Helper.findFirstEqual(RoadStatus[this.getPosition_channel()], this.getId());

		RoadStatus[this.getPosition_channel()][P] = 0;
		Init.getInstance().getCarsOnRoad().remove(this);
		Init.getInstance().setScheduledCars(Init.getInstance().getScheduledCars() + 1);
		this.setStatus(CarStatus.Stop);
	}

	/**
	 * get total number of the cars
	 * 
	 * @param RoadStatus
	 * @return
	 */
	private int getTheNumberOfCars(int[][] RoadStatus) {
		int count = 0;
		for (int i = 0; i < RoadStatus.length; i++) {
			for (int j = 0; j < RoadStatus[i].length; j++) {
				if (RoadStatus[i][j] != 0) {
					count++;
				}
			}
		}
		return count;
	}
}
