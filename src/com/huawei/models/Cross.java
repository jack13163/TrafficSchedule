package com.huawei.models;

public class Cross {
	private int id;
	private int uid;
	private int rid;
	private int did;
	private int lid;

	public Cross() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public int getLid() {
		return lid;
	}

	public void setLid(int lid) {
		this.lid = lid;
	}

	@Override
	public String toString() {
		return "Cross [id=" + id + ", uid=" + uid + ", rid=" + rid + ", did=" + did + ", lid=" + lid + "]";
	}

	public Cross(int id, int uid, int rid, int did, int lid) {
		super();
		this.id = id;
		this.uid = uid;
		this.rid = rid;
		this.did = did;
		this.lid = lid;
	}

}
