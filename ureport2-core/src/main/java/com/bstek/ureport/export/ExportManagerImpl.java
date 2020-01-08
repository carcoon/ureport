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
package com.bstek.ureport.export;

import java.util.List;
import java.util.Map;

import com.bstek.ureport.build.Dataset;
import com.bstek.ureport.build.paging.Page;
import com.bstek.ureport.cache.CacheUtils;
import com.bstek.ureport.chart.ChartData;
import com.bstek.ureport.definition.ReportDefinition;
import com.bstek.ureport.export.excel.high.ExcelProducer;
import com.bstek.ureport.export.excel.low.Excel97Producer;
import com.bstek.ureport.export.html.HtmlProducer;
import com.bstek.ureport.export.html.HtmlReport;
import com.bstek.ureport.export.pdf.PdfProducer;
import com.bstek.ureport.export.word.high.WordProducer;
import com.bstek.ureport.model.Report;

/**
 * @author Jacky.gao
 * @since 2016年12月4日
 */
public class ExportManagerImpl implements ExportManager {
	private ReportRender reportRender;
	private HtmlProducer htmlProducer=new HtmlProducer();
	private WordProducer wordProducer=new WordProducer();
	private ExcelProducer excelProducer=new ExcelProducer();
	private Excel97Producer excel97Producer=new Excel97Producer();
	private PdfProducer pdfProducer=new PdfProducer();

	@Override
	public HtmlReport exportHtml(String file, String contextPath, Map<String, Object> parameters, boolean loadall) {
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		int type=Dataset.DATASET_TYPE_BODY;
		if(loadall){
			type=Dataset.DATASET_TYPE_ALL;
		}
		Report report=reportRender.render(reportDefinition, parameters,type);
		Map<String, ChartData> chartMap=report.getContext().getChartDataMap();
		if(chartMap.size()>0){
			CacheUtils.storeChartDataMap(chartMap);
		}
		HtmlReport htmlReport=new HtmlReport();
		String content=htmlProducer.produce(report);
		htmlReport.setContent(content);
		if(reportDefinition.getPaper().isColumnEnabled()){
			htmlReport.setColumn(reportDefinition.getPaper().getColumnCount());
		}
		htmlReport.setStyle(reportDefinition.getStyle());
		if(loadall) {
			htmlReport.setSearchFormData(reportDefinition.buildSearchFormData(report.getContext().getDatasetMap(), parameters));
		}
		htmlReport.setReportAlign(report.getPaper().getHtmlReportAlign().name());
		htmlReport.setChartDatas(report.getContext().getChartDataMap().values());
		htmlReport.setHtmlIntervalRefreshValue(report.getPaper().getHtmlIntervalRefreshValue());
		return htmlReport;
	}

	@Override
	public HtmlReport exportHtml(String file, String contextPath, Map<String, Object> parameters, int pageIndex, boolean loadall) {
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		int type=Dataset.DATASET_TYPE_BODY;
		if(loadall){
			type=Dataset.DATASET_TYPE_ALL;
		}
		Report report=reportRender.render(reportDefinition, parameters,type);
		Map<String, ChartData> chartMap=report.getContext().getChartDataMap();
		if(chartMap.size()>0){
			CacheUtils.storeChartDataMap(chartMap);
		}
		SinglePageData pageData=PageBuilder.buildSinglePageData(pageIndex, report);
		List<Page> pages=pageData.getPages();
		String content=null;
		if(pages.size()==1){
			content=htmlProducer.produce(report.getContext(),pages.get(0),false);
		}else{
			content=htmlProducer.produce(report.getContext(),pages,pageData.getColumnMargin(),false);
		}
		HtmlReport htmlReport=new HtmlReport();
		htmlReport.setContent(content);
		if(reportDefinition.getPaper().isColumnEnabled()){
			htmlReport.setColumn(reportDefinition.getPaper().getColumnCount());
		}
		htmlReport.setStyle(reportDefinition.getStyle());
		//应该不是每次都这样，只有第一次需要加载
		if(loadall) {
			htmlReport.setSearchFormData(reportDefinition.buildSearchFormData(report.getContext().getDatasetMap(), parameters));
		}
		htmlReport.setPageIndex(pageIndex);
		htmlReport.setTotalPage(pageData.getTotalPages());
		htmlReport.setReportAlign(report.getPaper().getHtmlReportAlign().name());
		htmlReport.setChartDatas(report.getContext().getChartDataMap().values());
		htmlReport.setHtmlIntervalRefreshValue(report.getPaper().getHtmlIntervalRefreshValue());
		return htmlReport;
	}

	@Override
	public HtmlReport exportHtml(String file,String contextPath,Map<String, Object> parameters) {
		return exportHtml(file, contextPath, parameters, true);
	}
	
	@Override
	public HtmlReport exportHtml(String file,String contextPath,Map<String, Object> parameters, int pageIndex) {
		return exportHtml(file, contextPath, parameters, pageIndex, true);
	}
	@Override
	public void exportPdf(ExportConfigure config) {
		String file=config.getFile();
		Map<String, Object> parameters=config.getParameters();
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		Report report=reportRender.render(reportDefinition, parameters,Dataset.DATASET_TYPE_BODY);
		pdfProducer.produce(report, config.getOutputStream());
	}
	@Override
	public void exportWord(ExportConfigure config) {
		String file=config.getFile();
		Map<String, Object> parameters=config.getParameters();
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		Report report=reportRender.render(reportDefinition, parameters,Dataset.DATASET_TYPE_BODY);
		wordProducer.produce(report, config.getOutputStream());
	}
	@Override
	public void exportExcel(ExportConfigure config) {
		String file=config.getFile();
		Map<String, Object> parameters=config.getParameters();
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		Report report=reportRender.render(reportDefinition, parameters,Dataset.DATASET_TYPE_BODY);
		excelProducer.produce(report, config.getOutputStream());
	}
	
	@Override
	public void exportExcel97(ExportConfigure config) {
		String file=config.getFile();
		Map<String, Object> parameters=config.getParameters();
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		Report report=reportRender.render(reportDefinition, parameters,Dataset.DATASET_TYPE_BODY);
		excel97Producer.produce(report, config.getOutputStream());
	}
	
	@Override
	public void exportExcelWithPaging(ExportConfigure config) {
		String file=config.getFile();
		Map<String, Object> parameters=config.getParameters();
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		Report report=reportRender.render(reportDefinition, parameters,Dataset.DATASET_TYPE_BODY);
		excelProducer.produceWithPaging(report, config.getOutputStream());
	}
	@Override
	public void exportExcel97WithPaging(ExportConfigure config) {
		String file=config.getFile();
		Map<String, Object> parameters=config.getParameters();
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		Report report=reportRender.render(reportDefinition, parameters,Dataset.DATASET_TYPE_BODY);
		excel97Producer.produceWithPaging(report, config.getOutputStream());
	}
	
	@Override
	public void exportExcelWithPagingSheet(ExportConfigure config) {
		String file=config.getFile();
		Map<String, Object> parameters=config.getParameters();
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		Report report=reportRender.render(reportDefinition, parameters,Dataset.DATASET_TYPE_BODY);
		excelProducer.produceWithSheet(report, config.getOutputStream());
	}
	
	@Override
	public void exportExcel97WithPagingSheet(ExportConfigure config) {
		String file=config.getFile();
		Map<String, Object> parameters=config.getParameters();
		ReportDefinition reportDefinition=reportRender.getReportDefinition(file);
		Report report=reportRender.render(reportDefinition, parameters,Dataset.DATASET_TYPE_BODY);
		excel97Producer.produceWithSheet(report, config.getOutputStream());
	}
	
	public void setReportRender(ReportRender reportRender) {
		this.reportRender = reportRender;
	}
}
