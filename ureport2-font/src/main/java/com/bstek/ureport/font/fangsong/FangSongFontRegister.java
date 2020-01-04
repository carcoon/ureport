package com.bstek.ureport.font.fangsong;

import com.bstek.ureport.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class FangSongFontRegister implements FontRegister {
	@Override
	public String getFontName() {
		return "仿宋";
	}
	@Override
	public String getFontPath() {
		return "com/bstek/ureport/font/fangsong/SIMFANG.TTF";
	}
}
