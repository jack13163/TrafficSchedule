package com.huawei.dynamicprogramming;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.huawei.map.MapHelper;
import com.huawei.models.Cross;
import com.huawei.models.Road;

public class DynamicProgramming {
	private static int MAX = Integer.MAX_VALUE;
	private static int[][] pred = null;// 前驱矩阵

	static {

		int[][] M = createAMatrics();
		int[][] shortestDistance = getShortestDistance(M);
		pred = predecessor(M, shortestDistance);

	}

	/**
	 * 获取最短距离
	 * 
	 * @return
	 */
	private static int[][] getShortestDistance(int[][] W) {
		int n = W.length;
		int[][] L = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				L[i][j] = W[i][j];
			}
		}
		for (int i = 0; i < n - 2; i++) {
			L = extendShortestPaths(L, W);
		}
		return L;
	}

	/**
	 * 扩展路径
	 * 
	 * @param L
	 * @param M
	 * @return
	 */
	private static int[][] extendShortestPaths(int[][] L, int[][] W) {
		int n = L.length;
		int[][] LN = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				LN[i][j] = MAX;
				for (int k = 0; k < n; k++) {
					if (L[i][k] != MAX && W[k][j] != MAX) {
						LN[i][j] = Math.min(LN[i][j], L[i][k] + W[k][j]);
					}
				}
			}
		}

		return LN;
	}

	/**
	 * 根据最短路径权重矩阵smallest_length，计算前驱矩阵pred
	 * 
	 * @param edge_weights
	 * @param smallest_length
	 * @return
	 */
	private static int[][] predecessor(int[][] edge_weights, int[][] smallest_length) {
		int NIL = -1; // NIL表示不存在的顶点；
		int vertex_number = edge_weights.length;
		int[][] pred = new int[vertex_number][vertex_number];

		for (int i = 0; i != vertex_number; ++i)
			for (int j = 0; j != vertex_number; ++j)
				pred[i][j] = NIL;

		for (int i = 0; i != vertex_number; ++i) {
			for (int j = 0; j != vertex_number; ++j) {
				if (i != j && smallest_length[i][j] != MAX) {
					for (int k = 0; k != vertex_number; ++k) {
						if (smallest_length[i][k] != MAX && edge_weights[k][j] != MAX) {
							if (k != j && smallest_length[i][j] == smallest_length[i][k] + edge_weights[k][j]) {
								pred[i][j] = k;
								break;
							}
						}
					}
				}
			}
		}
		return pred;
	}

	/**
	 * 创建邻接矩阵
	 * 
	 * @return
	 */
	private static int[][] createAMatrics() {
		List<Road> roads = MapHelper.getInstance().getRoadList();
		List<Cross> crosses = MapHelper.getInstance().getCrossList();

		int numberOfCrosses = crosses.size();
		int[][] M = new int[numberOfCrosses][numberOfCrosses];

		// init
		for (int i = 0; i < numberOfCrosses; i++) {
			for (int j = 0; j < numberOfCrosses; j++) {
				if (i == j) {
					M[i][j] = 0;
				} else {
					M[i][j] = MAX;
				}
			}
		}

		for (Road road : roads) {
			int start = MapHelper.getInstance().getIdToNo().get(road.getSid());
			int end = MapHelper.getInstance().getIdToNo().get(road.getEid());
			int len = road.getLen();
			if (road.getDuplex() == 0) {
				M[start][end] = len;
			} else {
				M[start][end] = len;
				M[end][start] = len;
			}
		}
		return M;
	}

	/**
	 * 获取两点之间的最短路径(从0开始)
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static Queue<Road> getPath(int start, int end) {
		Queue<Road> roads = new LinkedList<Road>();
		Stack<Integer> cs = new Stack<Integer>();
		cs.push(end);

		boolean flag = false;

		while (true) {
			int startIndex = MapHelper.getInstance().getIdToNo().get(start);
			int endIndex = MapHelper.getInstance().getIdToNo().get(end);
			int t = MapHelper.getInstance().getNoToId().get(pred[startIndex][endIndex]);// end的前驱节点ID

			// not exist
			if (t == -1) {
				break;
			} else {
				cs.push(t);
			}

			// find it
			if (t == start) {
				flag = true;
				break;
			}
			end = t;// back
		}

		if (flag) {
			int first = cs.pop();

			while (!cs.isEmpty()) {
				int next = cs.pop();

				Road road = MapHelper.getInstance().getRoadByCrosses(first, next);
				roads.add(road);
				first = next;// keep relation
			}

			return roads;
		} else {
			return null;
		}
	}
}
