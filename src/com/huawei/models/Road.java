package com.huawei.models;

public class Road {
	private int id;
	private int len;
	private int speed;
	private int chanel;
	private int sid;
	private int eid;
	private int duplex;

	private boolean disabled;

	public boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Road() {
		// TODO Auto-generated constructor stub
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getChanel() {
		return chanel;
	}

	public void setChanel(int chanel) {
		this.chanel = chanel;
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

	public int getDuplex() {
		return duplex;
	}

	public void setDuplex(int duplex) {
		this.duplex = duplex;
	}

	public Road(int id, int len, int speed, int chanel, int sid, int eid, int duplex) {
		super();
		this.id = id;
		this.len = len;
		this.speed = speed;
		this.chanel = chanel;
		this.sid = sid;
		this.eid = eid;
		this.duplex = duplex;
	}

	@Override
	public String toString() {
		return "Road [id=" + id + ", len=" + len + ", speed=" + speed + ", chanel=" + chanel + ", sid=" + sid + ", eid="
				+ eid + ", duplex=" + duplex + "]";
	}
}
