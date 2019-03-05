package com.fanfanlicai.view.calendar;

import java.util.Date;

public class DateSignData {
	private Date date;
	private boolean isPrize;
	public DateSignData(){
		
	}
	public DateSignData(int year,int month,int day){
		this.date = new Date(year - 1900, month, day);
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isPrize() {
		return isPrize;
	}
	public void setPrize(boolean isPrize) {
		this.isPrize = isPrize;
	}
}
