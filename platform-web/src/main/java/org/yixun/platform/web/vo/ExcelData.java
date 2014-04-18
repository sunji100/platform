package org.yixun.platform.web.vo;

import java.util.LinkedHashMap;
import java.util.List;

public class ExcelData {
	private List<LinkedHashMap<String, Object>> head;
	private List<LinkedHashMap<String, Object>> body;
	public List<LinkedHashMap<String, Object>> getHead() {
		return head;
	}
	public void setHead(List<LinkedHashMap<String, Object>> head) {
		this.head = head;
	}
	public List<LinkedHashMap<String, Object>> getBody() {
		return body;
	}
	public void setBody(List<LinkedHashMap<String, Object>> body) {
		this.body = body;
	}
}
