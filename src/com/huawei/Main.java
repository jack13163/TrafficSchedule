package com.huawei;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.huawei.map.Init;
import com.huawei.map.MapHelper;
import com.huawei.models.Answer;
import com.huawei.models.Car;
import com.huawei.models.CarDirection;
import com.huawei.models.CarStatus;
import com.huawei.models.Cross;
import com.huawei.models.Road;
import com.huawei.utils.Helper;

public class Main {
	private static int MaxTM = 50;// detect dead lock
	public static int PolicyNum = 20;// the number of policy
	public static double PassRate = 0.5;// pass rate of each road.(default)
	public static int MaxCarsOnRoad = 5000;// max cars on the map.(default)
	public static int T = 1;

	public static int[] CarsScheduled = new int[2];
	private static int iii = 0;
	public static String carfile = "config/car.txt";
	public static String roadfile = "config/road.txt";
	public static String crossfile = "config/cross.txt";
	public static String answerfile = "config/answer.txt";

	public static void main(String[] args) {
		MaxTM = 50;// detect dead lock
		PolicyNum = 20;// the number of policy
		T = 1;

		CarsScheduled = new int[2];
		iii = 0;

		if (args != null) {
			carfile = args[0];
			roadfile = args[1];
			crossfile = args[2];
			answerfile = args[3];
		}

		while (true) {
			if (T % MaxTM == 0) {
				CarsScheduled[iii % 2] = Init.getInstance().getScheduledCars();
				iii++;

				if (Helper.sum(CarsScheduled) != 0 && Math.abs(CarsScheduled[0] - CarsScheduled[1]) < 1) {
					break;
				}
			}

			if (!Init.getInstance().getCarsOnRoad().isEmpty()) {
				for (Road road : MapHelper.getInstance().getRoadList()) {
					int NumberOfChannel = road.getChanel();
					int LenOfRoad = road.getLen();

					for (int k = 0; k < road.getDuplex() + 1; k++) {

						int[] AheadCar_EachChannel = new int[NumberOfChannel];
						int[] BehindCarState_EachChannel = new int[NumberOfChannel];

						int[][][] Road_Record = MapHelper.getInstance().getRoadStatus().get(road.getId());
						for (int i = LenOfRoad - 1; i >= 0; i--) {
							for (int j = 0; j < NumberOfChannel; j++) {
								if (Road_Record[k][j][i] != 0) {
									int CarID = Road_Record[k][j][i];
									Car car = Init.getInstance().getCarsHashMap().get(CarID);
									int V1 = Math.min(car.getSpeed(), road.getSpeed());

									if (AheadCar_EachChannel[j] == 0) {
										int S1 = LenOfRoad - Helper.findFirstEqual(Road_Record[k][j], CarID) - 1;// GetDistanceToAcrossRoad();
										Road nextRoad = car.getPath().peek();

										if (nextRoad != null) {
											int NextRoad_Speed = nextRoad.getSpeed();
											int V2 = Math.min(car.getSpeed(), NextRoad_Speed);

											int S2 = V2 - S1;
											if (S2 <= 0) {
												car.setStatus(CarStatus.Stop);
												BehindCarState_EachChannel[j] = 0;
											} else {
												car.setStatus(CarStatus.Wait);
												BehindCarState_EachChannel[j] = 1;
											}
										} else {
											if (V1 > S1) {
												car.setStatus(CarStatus.Wait);
												BehindCarState_EachChannel[j] = 1;
											} else {
												car.setStatus(CarStatus.Stop);
												BehindCarState_EachChannel[j] = 0;
											}
										}
									} else {
										int S1 = Helper.findFirstEqual(Road_Record[k][j], AheadCar_EachChannel[j])
												- Helper.findFirstEqual(Road_Record[k][j], CarID) - 1;
										if (BehindCarState_EachChannel[j] == 1 && V1 >= S1) {
											car.setStatus(CarStatus.Wait);
											BehindCarState_EachChannel[j] = 1;
										} else {
											car.setStatus(CarStatus.Stop);
											BehindCarState_EachChannel[j] = 0;
										}
									}

									AheadCar_EachChannel[j] = CarID;
								}
							}
						}
					}
				}

				for (Road road : MapHelper.getInstance().getRoadList()) {
					int[][][] Road_Record = MapHelper.getInstance().getRoadStatus().get(road.getId());
					int NumberOfChannel = road.getChanel();
					int LenOfRoad = road.getLen();

					for (int k = 0; k < road.getDuplex() + 1; k++) {
						for (int i = 0; i < NumberOfChannel; i++) {
							for (int j = LenOfRoad - 1; j >= 0; j--) {
								if (Road_Record[k][i][j] != 0) {
									int CarID = Road_Record[k][i][j];
									Car car = Init.getInstance().getCarsHashMap().get(CarID);

									if (car.getStatus() == CarStatus.Stop) {
										car.run(T);
									}
								}
							}
						}
					}
				}

				for (Cross cross : MapHelper.getInstance().getCrossList()) {

					int[] roads = new int[4];
					roads[0] = cross.getUid();
					roads[1] = cross.getRid();
					roads[2] = cross.getDid();
					roads[3] = cross.getLid();
					int[] oldRoad = roads.clone();
					Arrays.sort(roads);

					int CarCanAcross = 1;
					int[] CarCanAcross_EachRoad = new int[roads.length];

					while (CarCanAcross == 1) {
						Map<Integer, Queue<Car>> AcrossCarQueue_EachRoad = new HashMap<Integer, Queue<Car>>();

						for (int i = 0; i < roads.length; i++) {
							Queue<Car> cars = new LinkedList<Car>();

							if (roads[i] != -1) {
								Road road = MapHelper.getInstance().getRoadHashMap().get(roads[i]);
								int M = 0;
								if (road.getDuplex() == 1 && road.getSid() == cross.getId()) {
									M = 1;
								}
								int[][] roadStatus = MapHelper.getInstance().getRoadStatus().get(roads[i])[M];

								for (int j = roadStatus[0].length - 1; j >= 0; j--) {
									for (int k = 0; k < roadStatus.length; k++) {
										if (roadStatus[k][j] != 0) {
											Car car = Init.getInstance().getCarsHashMap().get(roadStatus[k][j]);
											if (car.getStatus() == CarStatus.Wait) {
												cars.add(car);
											}
										}
									}
								}
							}

							AcrossCarQueue_EachRoad.put(i, cars);
						}

						for (int i = 0; i < roads.length; i++) {
							if (CarCanAcross_EachRoad[i] == 0) {
								Queue<Car> cars = AcrossCarQueue_EachRoad.get(i);

								if (!cars.isEmpty()) {
									for (Car car : cars) {
										int CarID = car.getId();
										int channel = car.getPosition_channel();
										Road road = MapHelper.getInstance().getRoadHashMap().get(roads[i]);
										int LenOfRoad = road.getLen();

										int M = 0;
										if (car.getPosition_cross() == road.getEid()) {
											M = 1;
										}
										int[][] Road_Record = MapHelper.getInstance().getRoadStatus()
												.get(road.getId())[M];

										int P = Helper.findFirstEqual(Road_Record[channel], CarID);
										int S1 = LenOfRoad - P - 1;

										Road nextRoad = car.getPath().peek();

										if (nextRoad != null) {
											int NextRoad_Speed = nextRoad.getSpeed();
											int V2 = Math.min(car.getSpeed(), NextRoad_Speed);

											int S2 = V2 - S1;
											if (S2 <= 0) {
												car.run(T);
											} else {
												CarDirection Car_Direction = car.GetCarDirection();
												int nextCrossId = MapHelper.getInstance().getNextCrossId(car);
												int[][] NextRoadStatus = nextCrossId == nextRoad.getSid()
														? MapHelper.getInstance().getRoadStatus()
																.get(nextRoad.getId())[0]
														: MapHelper.getInstance().getRoadStatus()
																.get(nextRoad.getId())[1];
												int nextChannel = MapHelper.getInstance()
														.getRoadChannel(NextRoadStatus);

												if (nextChannel == -1) {
													car.run(T);
													break;
												} else {
													if (Car_Direction == CarDirection.Forward) {
														car.across(nextChannel, S2, T);
													} else if (Car_Direction == CarDirection.Left) {
														int r = Helper.findFirstEqual(oldRoad, roads[i]);
														int ConflictRoad = oldRoad[(r == 0) ? 3 : (r - 1)];

														if (ConflictRoad != -1) {
															int n = Helper.findFirstEqual(roads, ConflictRoad);
															Car conflictCar = AcrossCarQueue_EachRoad.get(n).peek();

															if (conflictCar != null && !conflictCar.getPath().isEmpty()
																	&& conflictCar
																			.GetCarDirection() == CarDirection.Forward) {
																break;
															}
														}

														car.across(nextChannel, S2, T);
													} else {
														int r1 = Helper.findFirstEqual(oldRoad, roads[i]);
														int ConflictRoad1 = oldRoad[(r1 == 3) ? 0 : (r1 + 1)];
														int r2 = Helper.findFirstEqual(oldRoad, roads[i]);
														int ConflictRoad2 = oldRoad[(r2 >= 2) ? (r2 - 2) : (r2 + 2)];

														if (ConflictRoad1 != -1) {
															int n = Helper.findFirstEqual(roads, ConflictRoad1);
															Car conflictCar1 = AcrossCarQueue_EachRoad.get(n).peek();

															if (conflictCar1 != null
																	&& !conflictCar1.getPath().isEmpty() && conflictCar1
																			.GetCarDirection() == CarDirection.Forward) {
																break;
															}
														}

														if (ConflictRoad2 != -1) {
															int n = Helper.findFirstEqual(roads, ConflictRoad2);
															Car conflictCar2 = AcrossCarQueue_EachRoad.get(n).peek();

															if (conflictCar2 != null
																	&& !conflictCar2.getPath().isEmpty() && conflictCar2
																			.GetCarDirection() == CarDirection.Left) {
																break;
															}
														}

														car.across(nextChannel, S2, T);
													}
												}
											}
										} else {
											car.run(T);
										}
									}
								} else {
									CarCanAcross_EachRoad[i] = 1;
								}
								if (Helper.sum(CarCanAcross_EachRoad) == CarCanAcross_EachRoad.length) {
									CarCanAcross = 0;
									break;
								}
							}
						}
					}
				}
			}

			for (Cross cross : MapHelper.getInstance().getCrossList()) {
				Queue<Car> cars = Init.getInstance().getCarPort().get(cross.getId());

				if (cars != null) {
					Iterator<Car> iterator = cars.iterator();
					while (iterator.hasNext()) {
						Car car = iterator.next();

						if (T >= car.getStime() && Init.getInstance().getCarsOnRoad().size() < MaxCarsOnRoad) {
							car.start(T);
							if (Init.getInstance().getCarsOnRoad().contains(car)) {
								iterator.remove();
							}
						} else {
							break;
						}
					}
				}
			}

			// complete
			if (Init.getInstance().getTotalCars() == Init.getInstance().getScheduledCars()) {
				try {
					PrintStream out = new PrintStream(Main.answerfile);
					for (Answer answer : Init.getInstance().getAnswers()) {
						out.println(answer);
					}
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
			T++;
			System.out.println("时刻" + T + "：" + Init.getInstance().getScheduledCars());
		}
	}
}
