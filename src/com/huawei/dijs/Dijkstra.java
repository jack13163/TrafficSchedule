package com.huawei.dijs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.huawei.map.MapHelper;
import com.huawei.models.Road;

public class Dijkstra {
	private List<Vertex> vertexList;
	private Map<String, List<Edge>> ver_edgeList_map;

	private LinkedList<Vertex> S;
	private LinkedList<Vertex> Q;

	public Dijkstra(List<Vertex> vertexList, Map<String, List<Edge>> ver_edgeList_map) {
		super();
		this.vertexList = vertexList;
		this.ver_edgeList_map = ver_edgeList_map;
	}

	public List<Vertex> getVertexList() {
		return vertexList;
	}

	public void setVertexList(List<Vertex> vertexList) {
		this.vertexList = vertexList;
	}

	public Map<String, List<Edge>> getVer_edgeList_map() {
		return ver_edgeList_map;
	}

	public void setVer_edgeList_map(Map<String, List<Edge>> ver_edgeList_map) {
		this.ver_edgeList_map = ver_edgeList_map;
	}

	/**
	 * ��ʼ��Դ��
	 * 
	 * @param v
	 */
	public void INITIALIZE_SINGLE_SOURCE(Vertex v) {
		v.setParent(null);
		v.setAdjuDist(0);
	}

	/**
	 * 
	 * @param startIndex
	 * @param destIndex
	 */
	public Queue<Road> dijkstraTravasal(int startIndex, int destIndex) {
		Vertex start = vertexList.get(startIndex);

		INITIALIZE_SINGLE_SOURCE(start);

		S = new LinkedList<>();
		Q = new LinkedList<>();
		for (int i = 0; i < vertexList.size(); i++) {
			Q.add(vertexList.get(i));
		}

		while (!Q.isEmpty()) {
			Vertex u = EXTRACT_MIN(Q);
			S.add(u);
			List<Edge> list = ver_edgeList_map.get(u.getName());
			for (Edge edge : list) {
				RELAX(edge);
			}
		}

		// showResult(startIndex, destIndex);
		return getResult(startIndex, destIndex);
	}

	/**
	 * 
	 * @param q
	 * @return
	 */
	private Vertex EXTRACT_MIN(LinkedList<Vertex> q) {
		if (q.isEmpty())
			return null;

		int min = 0;

		for (int i = 0; i < q.size(); i++) {
			if (q.get(min).getAdjuDist() > q.get(i).getAdjuDist()) {
				min = i;
			}
		}

		Vertex min_Vertex = q.remove(min);
		return min_Vertex;
	}

	/**
	 * @param edge
	 */
	public void RELAX(Edge edge) {
		Vertex v1 = edge.getStartVertex();
		Vertex v2 = edge.getEndVertex();
		int w = edge.getWeight();
		if (v2.getAdjuDist() > v1.getAdjuDist() + w) {
			v2.setAdjuDist(v1.getAdjuDist() + w);
			v2.setParent(v1);
		}
	}

	/**
	 */
	@SuppressWarnings("unused")
	private void showResult(int startIndex, int destIndex) {
		Stack<Vertex> routes = new Stack<>();
		Vertex v = vertexList.get(destIndex);
		while (v != null) {
			routes.push(v);
			v = v.getParent();
		}

		System.out.print("[" + (startIndex + 1) + ":" + (destIndex + 1) + "(" + vertexList.get(destIndex).getAdjuDist()
				+ ") ]: ");
		while (!routes.isEmpty()) {
			System.out.print(routes.pop().getName());
			if (!routes.isEmpty()) {
				System.out.print("-->");
			}
		}
		System.out.println();
	}

	/**
	 */
	private Queue<Road> getResult(int startIndex, int destIndex) {
		Stack<Integer> routes = new Stack<>();
		Vertex v = vertexList.get(destIndex);
		while (v != null) {
			routes.push(Integer.parseInt(v.getName()));
			v = v.getParent();
		}
		Queue<Road> roads = new LinkedList<>();
		int cur_cross = routes.pop();
		while (!routes.isEmpty()) {
			int next_cross = routes.pop();
			roads.add(MapHelper.getInstance().getRoadByCrosses(cur_cross, next_cross));
			cur_cross = next_cross;
		}

		return roads;
	}
}