package org.yixun.platform.web.test.vo;

import java.util.ArrayList;
import java.util.List;

public class ParamVo1 {
	private String a;
	private String b;
	private List<ParamVo> voList = new ArrayList<ParamVo>();
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public List<ParamVo> getVoList() {
		return voList;
	}
	public void setVoList(List<ParamVo> voList) {
		this.voList = voList;
	}
}
