package com.example.entity;

import java.io.Serializable;
import java.sql.Date;

public class WeiJinEntity implements Serializable {
	//��¼��ʾ��keyֵ
	private String weiJinIDString;
	private String userIDString;
	private String storeIDString;
	//΢���¼�����Ϣ
	private String storeName;
	private float Amount;
	private String briefInfoString;
	private Boolean isExchanged;
	private String exchangedTimeString;
	private String createTimeString;
	
	public WeiJinEntity(){
		isExchanged = false;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getWeiJinIDString() {
		return weiJinIDString;
	}
	public void setWeiJinIDString(String weiJinIDString) {
		this.weiJinIDString = weiJinIDString;
	}
	public String getUserIDString() {
		return userIDString;
	}
	public void setUserIDString(String userIDString) {
		this.userIDString = userIDString;
	}
	public String getStoreIDString() {
		return storeIDString;
	}
	public void setStoreIDString(String storeIDString) {
		this.storeIDString = storeIDString;
	}
	public float getAmount() {
		return Amount;
	}
	public void setAmount(float amount) {
		Amount = amount;
	}
	public String getBriefInfoString() {
		return briefInfoString;
	}
	public void setBriefInfoString(String briefInfoString) {
		this.briefInfoString = briefInfoString;
	}
	public Boolean getIsExchanged() {
		return isExchanged;
	}
	public void setIsExchanged(Boolean isExchanged) {
		this.isExchanged = isExchanged;
	}
	public String getExchangedTimeString() {
		return exchangedTimeString;
	}
	public void setExchangedTimeString(String exchangedTimeString) {
		this.exchangedTimeString = exchangedTimeString;
	}
	public String getCreateTimeString() {
		return createTimeString;
	}
	public void setCreateTimeString(String createTimeString) {
		this.createTimeString = createTimeString;
	}
	
}
