package com.jiezhi.lib.model;

import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "student_info")
public class StudentInfo {
	
	private int id;
	private String name;// 姓名
	private String number;// 证件号
	private String sumBorrowed;// 借阅总数
	private String tel;// 联系方式
	private String sex;// 性别
	private String maxBorrow;// 最大可借图书
	private String education;// 文化程度
	private String wockPlace;// 工作单位
	private String idNumber;// 身份证号码
	private String grade;// 借阅等级（自定义）
	private String toExpire;// 五天内要超期的图书
	private String expired;// 已超期的图书
	private String expireData;// 证件失效日期

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSumBorrowed() {
		return sumBorrowed;
	}

	public void setSumBorrowed(String sumBorrowed) {
		this.sumBorrowed = sumBorrowed;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMaxBorrow() {
		return maxBorrow;
	}

	public void setMaxBorrow(String maxBorrow) {
		this.maxBorrow = maxBorrow;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getWockPlace() {
		return wockPlace;
	}

	public void setWockPlace(String wockPlace) {
		this.wockPlace = wockPlace;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getToExpire() {
		return toExpire;
	}

	public void setToExpire(String toExpire) {
		this.toExpire = toExpire;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	public String getExpireData() {
		return expireData;
	}

	public void setExpireData(String expireData) {
		this.expireData = expireData;
	}

	@Override
	public String toString() {
		return "StudentInfo [name=" + name + ", number=" + number
				+ ", sumBorrowed=" + sumBorrowed + ", tel=" + tel + ", sex="
				+ sex + ", maxBorrow=" + maxBorrow + ", education=" + education
				+ ", wockPlace=" + wockPlace + ", idNumber=" + idNumber
				+ ", grade=" + grade + ", toExpire=" + toExpire + ", expired="
				+ expired + ", expireData=" + expireData + "]";
	}

}
