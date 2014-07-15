package com.example.entity;

import java.io.Serializable;

import android.graphics.Bitmap;

public final class McItem implements Serializable{
	private String mcId;
	private String mcName;
	private String mcLogoUrl;
	private String mcAddress;
	private String mcTelephone;
	private String mcIntro;
	private String mcSlogen;
	private Boolean isSubscribed = false;
	
	public McItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public McItem(McItem item){
		mcId = item.getMcId();
		mcName = item.getMcName();
		mcLogoUrl = item.getMcLogoUrl();
		mcAddress = item.getMcAddress();
		mcTelephone = item.mcTelephone;
		mcIntro = item.getMcIntro();
		mcSlogen = item.getMcSlogen();
		isSubscribed = item.getIsSubscribed();
	}
	public McItem(String mcId, String mcName, String gotoIcon, String mcAddress,String mcTelephone,String mcIntro,String mcSlogen) {
		super();
		this.mcId = mcId;
		this.mcName = mcName;
		this.mcLogoUrl = gotoIcon;
		this.mcAddress = mcAddress;
		this.mcTelephone = mcTelephone;
		this.mcIntro = mcIntro;
		this.mcSlogen = mcSlogen;
	}
	public String getMcId() {
		return mcId;
	}
	public void setMcId(String mcId) {
		this.mcId = mcId;
	}
	public String getMcName() {
		return mcName;
	}
	public void setMcName(String mcName) {
		this.mcName = mcName;
	}
	public String getMcLogoUrl() {
		return mcLogoUrl;
	}
	public void setMcLogoUrl(String mcLogoUrl) {
		this.mcLogoUrl = mcLogoUrl;
	}
	public String getMcAddress() {
		return mcAddress;
	}
	public void setMcAddress(String mcAddress) {
		this.mcAddress = mcAddress;
	}
	public Boolean getIsSubscribed() {
		return isSubscribed;
	}
	public void setIsSubscribed(Boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}
	public String getMcTelephone() {
		return mcTelephone;
	}
	public void setMcTelephone(String mcTelephone) {
		this.mcTelephone = mcTelephone;
	}
	public String getMcIntro() {
		return mcIntro;
	}
	public void setMcIntro(String mcIntro) {
		this.mcIntro = mcIntro;
	}
	public String getMcSlogen() {
		return mcSlogen;
	}
	public void setMcSlogen(String mcSlogen) {
		this.mcSlogen = mcSlogen;
	}
	@Override
	public String toString() {
		return "McItem [mcId=" + mcId + ", mcName=" + mcName + ", mcLogoUrl="
				+ mcLogoUrl + ", mcAddress=" + mcAddress + ", mcTelephone="
				+ mcTelephone + ", mcIntro=" + mcIntro + ", mcSlogen="
				+ mcSlogen + ", isSubscribed=" + isSubscribed + "]";
	}
	
	

}
