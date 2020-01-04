package com.bstek.ureport.font.yahei;

import com.bstek.ureport.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class YaheiFontRegister implements FontRegister {
	@Override
	public String getFontName() {
		return "微软雅黑";
	}
	@Override
	public String getFontPath() {
		return "com/bstek/ureport/font/yahei/msyh.ttc";
	}
}
