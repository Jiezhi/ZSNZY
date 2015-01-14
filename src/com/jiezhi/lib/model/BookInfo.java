package com.jiezhi.lib.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "book_info")
public class BookInfo {

	@Id(column = "marc")
	private String marc;
	private String barCode;// 条码号
	private String title;// 题名
	private String author;// 作者
	private String isbn;// ISBN
	private String imageUrl;// 图片地址
	private String price;// 价格
	private String dbDetail;// 豆瓣简介
	private String publicationType;// 文献类型
	private String viewCount;// 浏览次数
	private String borrowingCount;// 借阅次数
	private String score;// 评价

	public String getMarc() {
		return marc;
	}

	public void setMarc(String marc) {
		this.marc = marc;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDbDetail() {
		return dbDetail;
	}

	public void setDbDetail(String dbDetail) {
		this.dbDetail = dbDetail;
	}

	public String getPublicationType() {
		return publicationType;
	}

	public void setPublicationType(String publicationType) {
		this.publicationType = publicationType;
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public String getBorrowingCount() {
		return borrowingCount;
	}

	public void setBorrowingCount(String borrowingCount) {
		this.borrowingCount = borrowingCount;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "BookInfo [marc=" + marc + ", title=" + title + ", author="
				+ author + ", isbn=" + isbn + ", imageUrl=" + imageUrl
				+ ", price=" + price + ", dbDetail=" + dbDetail
				+ ", publicationType=" + publicationType + ", viewCount="
				+ viewCount + ", borrowingCount=" + borrowingCount + ", score="
				+ score + "]";
	}

}
