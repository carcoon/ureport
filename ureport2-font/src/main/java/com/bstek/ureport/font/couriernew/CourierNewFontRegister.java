package com.bstek.ureport.font.couriernew;

import com.bstek.ureport.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class CourierNewFontRegister implements FontRegister {
	@Override
	public String getFontName() {
		return "Courier New";
	}
	@Override
	public String getFontPath() {
		return "com/bstek/ureport/font/couriernew/COUR.TTF";
	}
}
