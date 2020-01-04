package com.bstek.ureport.font.impact;

import com.bstek.ureport.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class ImpactFontRegister implements FontRegister {
	@Override
	public String getFontName() {
		return "Impact";
	}
	@Override
	public String getFontPath() {
		return "com/bstek/ureport/font/impact/IMPACT.TTF";
	}
}
