package com.aires.bean;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.aires.base.bean.Bean;
/**
 * 
 * @author aires
 * 2017年9月21日 下午2:35:43
 * 描述： 
 */
@Entity
@Table(name = "P_USER_COIN")
public class Coin extends Bean{
	private String userId;
	private int count;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
