package com.huawei.dijs;

public class Vertex {
	private final static int infinite_dis = Integer.MAX_VALUE;

	private String name;
	private int adjuDist;
	private Vertex parent;

	public Vertex() {
		this.adjuDist = infinite_dis;
		this.parent = null;
	}

	public Vertex(String name) {
		this.adjuDist = infinite_dis;
		this.parent = null;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAdjuDist() {
		return adjuDist;
	}

	public void setAdjuDist(int adjuDist) {
		this.adjuDist = adjuDist;
	}

	public Vertex getParent() {
		return parent;
	}

	public void setParent(Vertex parent) {
		this.parent = parent;
	}

	/**
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vertex)) {
			throw new ClassCastException("an object to compare with a Vertext must be Vertex");
		}

		if (this.name == null) {
			throw new NullPointerException("name of Vertex to be compared cannot be null");
		}

		return this.name.equals(obj);
	}
}
