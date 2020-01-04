package com.bstek.ureport.font.comicsansms;

import com.bstek.ureport.export.pdf.font.FontRegister;

/**
 * @author Jacky.gao
 * @since 2014年5月7日
 */
public class ComicSansMSFontRegister implements FontRegister {
	@Override
	public String getFontName() {
		return "Comic Sans MS";
	}
	@Override
	public String getFontPath() {
		return "com/bstek/ureport/font/comicsansms/COMIC.TTF";
	}
}
