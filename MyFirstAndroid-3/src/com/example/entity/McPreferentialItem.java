package com.example.entity;

import java.io.Serializable;


import com.example.myfirstandroid.R;


public class McPreferentialItem implements Serializable{
	private String mcId;
	private String mcName;
	private String prefId;
	private String prefImageUrl;
	private String prefTitle;
	private String prefDescription;
	private String prefTime;
	
	
	public McPreferentialItem(String mcId,String mcName, String prefId, String gotoIcon,String prefTitle,
			String prefDescription, String prefTime) {
		super();
		this.mcId = mcId;
		this.mcName = mcName;
		this.prefId = prefId;
		this.prefTitle = prefTitle;
		this.prefDescription = prefDescription;
		this.prefTime = prefTime;
		this.prefImageUrl = gotoIcon;
	}
	
	
	public McPreferentialItem() {
		super();
		// TODO Auto-generated constructor stub
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
	public String getPrefId() {
		return prefId;
	}
	public void setPrefId(String prefId) {
		this.prefId = prefId;
	}
	public String getPrefTitle() {
		return prefTitle;
	}
	public void setPrefTitle(String prefTitle) {
		this.prefTitle = prefTitle;
	}
	public String getPrefDescription() {
		return prefDescription;
	}
	public void setPrefDescription(String prefDescription) {
		this.prefDescription = prefDescription;
	}
	public String getPrefTime() {
		return prefTime;
	}
	public void setPrefTime(String prefTime) {
		this.prefTime = prefTime;
	}
	public String getPrefImageUrl() {
		return prefImageUrl;
	}
	public void setPrefImageUrl(String prefImageUrl) {
		this.prefImageUrl = prefImageUrl;
	}
	
	

}
