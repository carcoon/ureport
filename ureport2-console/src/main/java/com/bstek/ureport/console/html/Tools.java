/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.bstek.ureport.console.html;

/**
 * @author Jacky.gao
 * @since 2017年6月1日
 */
public class Tools {
	private boolean show=true;
	private boolean word=true;
	private boolean excel=true;
	private boolean pdf=true;
	private boolean pagingExcel=true;
	private boolean sheetPagingExcel=true;
	private boolean print=true;
	private boolean pdfPrint=true;
	private boolean pdfPreviewPrint=true;
	private boolean excel97=true;
	private boolean pagingExcel97=true;
	private boolean sheetPagingExcel97=true;
	private boolean paging=true;
	public Tools(boolean init) {
		if(init){
			word=true;
			excel=true;
			pdf=true;
			pagingExcel=true;
			sheetPagingExcel=true;
			print=true;
			pdfPrint=true;
			pdfPreviewPrint=true;
			paging=true;
			excel97=true;
			pagingExcel97=true;
			sheetPagingExcel97=true;
		}else{
			word=false;
			excel=false;
			pdf=false;
			pagingExcel=false;
			sheetPagingExcel=false;
			print=false;
			pdfPrint=false;
			pdfPreviewPrint=false;
			paging=false;
			excel97=false;
			pagingExcel97=false;
			sheetPagingExcel97=false;
		}
	}
	public void doInit(String name){
		if(name.equals("5")){
			word=true;
		}else if(name.equals("6")){
			excel=true;
		}else if(name.equals("4")){
			pdf=true;
		}else if(name.equals("1")){
			print=true;
		}else if(name.equals("2")){
			pdfPrint=true;
		}else if(name.equals("3")){
			pdfPreviewPrint=true;
		}else if(name.equals("9")){
			paging=true;
		}else if(name.equals("7")){
			pagingExcel=true;
		}else if(name.equals("8")){
			sheetPagingExcel=true;
		}else if(name.equals("10")){
			excel97=true;
		}else if(name.equals("11")){
			pagingExcel97=true;
		}else if(name.equals("12")){
			sheetPagingExcel97=true;
		}

	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public boolean isWord() {
		return word;
	}
	public void setWord(boolean word) {
		this.word = word;
	}
	public boolean isExcel() {
		return excel;
	}
	public void setExcel(boolean excel) {
		this.excel = excel;
	}
	public boolean isPdf() {
		return pdf;
	}
	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}
	public boolean isPagingExcel() {
		return pagingExcel;
	}
	public void setPagingExcel(boolean pagingExcel) {
		this.pagingExcel = pagingExcel;
	}
	public boolean isSheetPagingExcel() {
		return sheetPagingExcel;
	}
	public void setSheetPagingExcel(boolean sheetPagingExcel) {
		this.sheetPagingExcel = sheetPagingExcel;
	}

	public boolean isExcel97() {
		return excel97;
	}

	public void setExcel97(boolean excel97) {
		this.excel97 = excel97;
	}

	public boolean isPagingExcel97() {
		return pagingExcel97;
	}

	public void setPagingExcel97(boolean pagingExcel97) {
		this.pagingExcel97 = pagingExcel97;
	}

	public boolean isSheetPagingExcel97() {
		return sheetPagingExcel97;
	}

	public void setSheetPagingExcel97(boolean sheetPagingExcel97) {
		this.sheetPagingExcel97 = sheetPagingExcel97;
	}

	public boolean isPrint() {
		return print;
	}
	public void setPrint(boolean print) {
		this.print = print;
	}
	public boolean isPdfPrint() {
		return pdfPrint;
	}
	public void setPdfPrint(boolean pdfPrint) {
		this.pdfPrint = pdfPrint;
	}
	public boolean isPdfPreviewPrint() {
		return pdfPreviewPrint;
	}
	public void setPdfPreviewPrint(boolean pdfPreviewPrint) {
		this.pdfPreviewPrint = pdfPreviewPrint;
	}
	public boolean isPaging() {
		return paging;
	}
	public void setPaging(boolean paging) {
		this.paging = paging;
	}
}
