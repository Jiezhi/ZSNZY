package com.jiezhi.lib.model;

import org.jsoup.select.Elements;

public class BookInfo_Temp {
	private String imageURL;
	private Elements introduction;
	private Elements guancang;

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Elements getIntroduction() {
		return introduction;
	}

	public void setIntroduction(Elements introduction) {
		this.introduction = introduction;
	}

	public Elements getGuancang() {
		return guancang;
	}

	public void setGuancang(Elements guancang) {
		this.guancang = guancang;
	}

	@Override
	public String toString() {
		return "BookInfo_Temp [imageURL=" + imageURL + ", introduction="
				+ introduction + ", guancang=" + guancang + "]";
	}

}
