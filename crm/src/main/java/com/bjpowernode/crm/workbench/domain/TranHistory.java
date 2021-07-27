package com.bjpowernode.crm.workbench.domain;

public class TranHistory {
	
	private String id;
	private String stage;  //此交易阶段
	private String money;
	private String expectedDate;  //预计成交日期
	private String createTime;  //此交易的创建时间
	private String createBy;    //此交易的创建人
	private String tranId;  //此交易id    外键

	private String kenengxing;

	public String getKenengxing() {
		return kenengxing;
	}

	public void setKenengxing(String kenengxing) {
		this.kenengxing = kenengxing;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getExpectedDate() {
		return expectedDate;
	}
	public void setExpectedDate(String expectedDate) {
		this.expectedDate = expectedDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	
	
}
