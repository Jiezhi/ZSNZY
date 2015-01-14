package com.jiezhi.lib.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jiezhi.lib.model.BookInfo;
import com.jiezhi.lib.model.BookInfo_Temp;
import com.jiezhi.lib.model.StudentInfo;

public class JsoupUtil {

	public static Double sumNumber;
	public static String pageNumber;
	public static String preUrl;
	public static String nextUrl;

	public static int page = 1;// 默认第一页

	public static void clearInfo() {
		page = 1;
		sumNumber = 0d;
		pageNumber = null;
		preUrl = null;
		nextUrl = null;
	}

	// 检索结果
	public static List<Map<String, Object>> searchBook(String html) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> bookMap;

		Document doc;
		try {

			// 通过Jsoup的connect（）方法，将html转化成Document
			doc = Jsoup.connect(html).timeout(30 * 1000).get();
			System.out.println("Success to parse!");

			// 判断“本馆没有您检索的纸本馆藏书目”
			String err = doc.select("h3").text().toString();
			if (err.equals("本馆没有您检索的纸本馆藏书目"))
				return null;

			// 获取图书总数以及页码总数
			sumNumber = getSumNumber(doc);
			pageNumber = getPageNumber(doc);
			// 获得前后页的链接
			if (sumNumber >= 20)
				getPreAndNextUrl(doc);

			Elements books = doc.select("tr[bgcolor=#FFFFFF]");
			Iterator<Element> book = books.iterator();
			while (book.hasNext()) {
				Element em = book.next();
				System.out.println(em.text());
				// 这里的bookMap每次都要实例化一个，否则将会出现所有的内容都是最后一条的内容
				bookMap = new HashMap<String, Object>();
				/*
				 * 经过多次验证，用Element(s)的text（）方法输出不带原来html的标签，而用toString的方法则会带标签
				 * 用html（）方法得到标签括起来的内容
				 */

				// 解析图书部分内容
				Elements bookInfo = em.select("td");
				int totalTds = bookInfo.size();
				for (int j = 0; j < totalTds; j++) {
					switch (j) {
					case 0:// 图书序号
						bookMap.put("bookNo", bookInfo.get(j).html().toString());
						break;
					case 1:// 标题和链接
						bookMap.put("bookTitle", bookInfo.get(j).text());
						bookMap.put("bookDetail", bookInfo.get(j).select("a")
								.attr("href").toString());
						break;
					case 2:// 作者
						bookMap.put("bookAuthor", bookInfo.get(j).text());
						break;
					case 3:// 出版社
						bookMap.put("bookPublisher", bookInfo.get(j).text());
						break;
					case 4:// 索书号
						bookMap.put("bookCallno", bookInfo.get(j).text());
						break;
					case 5:// 文献类型
						bookMap.put("bookType", bookInfo.get(j).text());
						break;
					default:
						break;
					}
				}
				list.add(bookMap);
			}
		} catch (IOException e) {
			// 解析失败！
			e.printStackTrace();
			System.out.println("Failed to Parse!");
		}
		return list;
	}

	// 获得前后页的链接
	private static void getPreAndNextUrl(Document doc) {
		// TODO Auto-generated method stub

		Elements hrefs = doc.select("span[class=pagination]").select(
				"a[class=blue]");

		System.out.println("herfs:" + hrefs);
		if (page <= 1) {
			nextUrl = hrefs.get(0).attr("abs:href");
		}
		if (page >= Math.ceil(sumNumber / 20)) {
			preUrl = hrefs.get(0).attr("abs:href");
		}
		if ((page > 1) && (page < Math.ceil(sumNumber / 20))) {
			preUrl = hrefs.get(0).attr("abs:href");
			nextUrl = hrefs.get(1).attr("abs:href");
		}
		System.out.println("preUrl:" + preUrl);
		System.out.println("nextUrl:" + nextUrl);

	}

	// 获得页码总数
	private static String getPageNumber(Document doc) {
		// TODO Auto-generated method stub
		// 判断是否为多页。（每页默认显示20条数据）
		if (sumNumber <= 20) {
			return "1/1";
		} else {
			return doc.select("span[class=pagination]").select("b").get(0)
					.text().toString();
		}

	}

	// 获得图书总数
	private static Double getSumNumber(Document doc) {
		// TODO Auto-generated method stubs
		return Double.parseDouble(doc.select("strong[class=red]").text()
				.toString());
	}

	// 登录
	public static StudentInfo loginUrl(String number, String passwd) {
		// TODO Auto-generated method stub

		String html = UrlUtil.LOGIN_URL + "?" + "number=" + number + "&passwd="
				+ passwd + "&select=cert_no&returnUrl=";
		System.out.println(html);
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(html);
		try {
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
			// int state = response.getStatusLine().getStatusCode();
			// System.out.println("state:" + state);

			// 读取cookie
			UrlUtil.cookies = ((AbstractHttpClient) client).getCookieStore()
					.getCookies();

			if (UrlUtil.cookies.isEmpty()) {
				System.out.println("-------Cookie NONE---------");
			} else {
				System.out.println(UrlUtil.cookies.size());
				for (int i = 0; i < UrlUtil.cookies.size(); i++) {

					System.out.println("cookies:"
							+ UrlUtil.cookies.get(i).getValue());
				}
			}

			// 写入cookie
			// html = GlobleData.BOOK_LST + "?" + "Cookie=PHPSESSID="
			// + GlobleData.cookies.get(0).getValue();
			// System.out.println(html);

			Document doc = Jsoup.connect(html).timeout(30 * 1000).get();
			// System.out.println("doc:" + doc);
			// 判断是否登录成功
			String status = doc.select("a[href=../reader/login.php]").text()
					.toString();

			if (status.equals("登录")) {
				System.out.println("登录失败，请检查账号和密码！");
				return null;
			} else {
				System.out.println("登录成功！");
				StudentInfo mStudent = new StudentInfo();
				Elements e = doc.select("strong[style=color:#F00;]");
				// 目前只要即将以及已经过期的图书的数据
				for (int i = 0; i <= 1; i++) {
					switch (i) {
					case 0:
						mStudent.setToExpire(e.get(i).text());
						break;
					case 1:
						mStudent.setExpired(e.get(i).text());
						break;
					case 2:
						break;
					case 3:
						break;
					default:
						break;
					}
				}
				e = doc.select("div[id=mylib_info]");// <div id="mylib_info" >
				Iterator<Element> its = e.select("td").iterator();
				int i = -1;// 不想改下面的，就改这里了
				while (its.hasNext()) {
					Element em = its.next();
					// 删去标签里不要的内容
					em.select("span[class=bluetext]").remove();

					switch (i) {
					case 0:// 姓名
						mStudent.setName(em.text());
						System.out.println("name:" + em.text());
						break;
					case 1:// 证件号
						mStudent.setNumber(em.text());
						System.out.println("number:" + em.text());
						break;
					case 2:// 条码号
						break;
					case 3:// 失效日期
						mStudent.setExpireData(em.text());
						System.out.println("失效日期:" + em.text());
						break;
					case 4:// 办证日期
						break;
					case 5:// 生效日期
						break;
					case 6:// 最大可借图书
						mStudent.setMaxBorrow(em.text());
						System.out.println("最大可借图书:" + em.text());
						break;
					case 7:// 最大可预约图书
						break;
					case 8:// 最大可委托图书
						break;
					case 9:// 读者类型
						mStudent.setEducation(em.text());
						break;
					case 10:// 借阅等级
						break;
					case 11:// 累计借书
						mStudent.setSumBorrowed(em.text());
						break;
					case 12:// 违章次数
						break;
					case 13:// 欠款金额
						break;
					case 14:// 系别
						break;
					case 15:// 邮箱
						break;
					case 16:// 身份证号
						mStudent.setIdNumber(em.text());
						System.out.println("身份证号:" + em.text());
						break;
					case 17:// 工作单位
						mStudent.setWockPlace(em.text());
						System.out.println("工作单位:" + em.text());
						break;
					case 18:// 职称
						break;
					case 19:// 职位
						break;
					case 20:// 性别
						mStudent.setSex(em.text());
						System.out.println("性别:" + em.text());
						break;
					case 21:// 住址
						break;
					case 22:// 邮编
						break;
					case 23:// 电话
						break;
					case 24:// 手机
						mStudent.setTel(em.text());
						break;
					case 25:// 出生日期
						break;
					case 26:// 文化程度
						break;
					case 27:// 押金
						break;
					case 28:// 手续费
						break;
					default:
						break;
					}
					i++;
				}
				System.out.println(i);
				return mStudent;
			}
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

	}

	// 获得当前借阅的数据
	public static List<Map<String, Object>> getBorrowedBook() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> bookMap;
		try {
			Document doc = Jsoup.connect(UrlUtil.BOOK_LST)
					.cookie("PHPSESSID", UrlUtil.cookies.get(0).getValue())
					.timeout(30 * 1000).get();
			String err = doc.select("strong[class=iconerr]").text().toString();
			if (err.equals("您的该项记录为空！"))
				return null;
			Elements es = doc.select("tr");

			Iterator<Element> book = es.iterator();
			book.next();
			while (book.hasNext()) {
				Element e = book.next();
				bookMap = new HashMap<String, Object>();
				Elements bookInfo = e.select("td");
				// System.out.println(bookInfo.toString());
				for (int i = 0; i < bookInfo.size(); i++) {
					// System.out.println(bookInfo.get(i).text());
					switch (i) {
					case 0:
						bookMap.put("barcode", bookInfo.get(i).text());
						break;
					case 1:
						bookMap.put("booktitle", bookInfo.get(i).text());
						bookMap.put("bookDetail", bookInfo.get(i).select("a")
								.attr("href").toString());
						break;
					case 2:
						bookMap.put("borrowedDate", bookInfo.get(i).text());
						break;
					case 3:
						bookMap.put("paybackDate", bookInfo.get(i).text());
						break;
					case 4:
						break;
					case 5:
						break;
					default:
						break;
					}
				}
				list.add(bookMap);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	// 热门图书
	public static List<Map<String, String>> getHotBook(char code) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		Document doc;

		try {
			doc = Jsoup.connect(UrlUtil.MAIN_SEARCH_URL).timeout(30 * 1000)
					.get();
			Elements es;
			if (code == 'a') {
				es = doc.select("div[ID=search_container_center]").select("li");
				Iterator<Element> i = es.iterator();
				while (i.hasNext()) {
					Element e = i.next();
					map = new HashMap<String, String>();
					map.put("title", e.text());
					map.put("bookDetail", e.select("a").attr("href").toString());
					list.add(map);
				}
			} else if (code == 'b') {
				es = doc.select("div[id=search_container_right]").select("li");
				Iterator<Element> i = es.iterator();
				while (i.hasNext()) {
					Element e = i.next();
					map = new HashMap<String, String>();
					map.put("title", e.text());
					map.put("bookDetail", e.select("a").attr("href").toString());
					list.add(map);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Load Failed!");
		}
		return list;
	}

	public static BookInfo_Temp loadDetail(String string) {
		// TODO Auto-generated method stub
		BookInfo_Temp bookInfo = null;
		String url = string;
		Document doc;
		try {
			doc = Jsoup.connect(url).timeout(30 * 1000).get();
			System.out.println("Doc:"
					+ doc.toString());
			
			bookInfo = new BookInfo_Temp();
			bookInfo.setImageURL(doc.select("img[id=book_img]").text());
			bookInfo.setIntroduction(doc.select("div[id=item_detail]").select(
					"dl[class=booklist]"));
			bookInfo.setGuancang(doc.select("tr[class=whitetext]"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookInfo;
	}

	// 由于从图书馆检索结果页面得不到ISBN号码，想要调用豆瓣API还得点击具体页面获取ISBN号，较为浪费流量，故暂且改为webview实现 //
	// 解析book详细信息
	public static BookInfo loadDetail2(String html) {
		BookInfo bookInfo = null;
		Document doc;
		System.out.println("LoadDetail!");
		try {
			doc = Jsoup.connect(html).timeout(30 * 1000).get();
			bookInfo = new BookInfo();
			bookInfo.setImageUrl(doc.select("img[id=img_book]").text());
			System.out
					.println("imgurl" + doc.select("img[id=img_book]").text());
		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed LoadDetail!");
		}
		return bookInfo;
	}
}
