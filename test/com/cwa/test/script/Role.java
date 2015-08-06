package com.cwa.test.script;

public class Role {
	private int hp;
	private int mp;

	private int maxHp;
	private int maxMp;

	public Role() {
		hp = 0;
		mp = 0;
		maxHp = 0;
		maxMp = 0;
	}

	@Override
	public String toString() {
		return "Role [hp=" + hp + ", mp=" + mp + ", maxHp=" + maxHp + ", maxMp=" + maxMp + "]";
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMp() {
		return mp;
	}

	public void setMp(int mp) {
		this.mp = mp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getMaxMp() {
		return maxMp;
	}

	public void setMaxMp(int maxMp) {
		this.maxMp = maxMp;
	}

}
