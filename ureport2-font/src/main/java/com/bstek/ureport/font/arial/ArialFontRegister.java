package com.bstek.ureport.font.arial;

import com.bstek.ureport.export.pdf.font.FontRegister;


/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class ArialFontRegister implements FontRegister {
	@Override
	public String getFontName() {
		return "Arial";
	}
	@Override
	public String getFontPath() {
		return "com/bstek/ureport/font/arial/ARIAL.TTF";
	}
}
