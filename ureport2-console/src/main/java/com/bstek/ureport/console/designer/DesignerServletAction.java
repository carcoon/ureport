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
package com.bstek.ureport.console.designer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bstek.ureport.console.cache.CacheMessageService;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import com.bstek.ureport.cache.CacheUtils;
import com.bstek.ureport.console.RenderPageServletAction;
import com.bstek.ureport.console.exception.ReportDesignException;
import com.bstek.ureport.definition.ReportDefinition;
import com.bstek.ureport.dsl.ReportParserLexer;
import com.bstek.ureport.dsl.ReportParserParser;
import com.bstek.ureport.dsl.ReportParserParser.DatasetContext;
import com.bstek.ureport.export.ReportRender;
import com.bstek.ureport.expression.ErrorInfo;
import com.bstek.ureport.expression.ScriptErrorListener;
import com.bstek.ureport.parser.ReportParser;
import com.bstek.ureport.provider.report.ReportProvider;

/**
 * @author Jacky.gao
 * @since 2017年1月25日
 */
public class DesignerServletAction extends RenderPageServletAction {
	private ReportRender reportRender;
	private ReportParser reportParser;
	private List<ReportProvider> reportProviders=new ArrayList<ReportProvider>();
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			// ureport/designer/loadreport 等其他方法
			invokeMethod(method, req, resp);
		}else{
			// ureport/designer 默认方法
			VelocityContext context = new VelocityContext();
			context.put("contextPath", req.getContextPath());
			resp.setContentType("text/html");
			resp.setCharacterEncoding("utf-8");
			Template template=ve.getTemplate("ureport-html/designer.html","utf-8");
			PrintWriter writer=resp.getWriter();
			template.merge(context, writer);
			writer.close();
		}
	}
	public void scriptValidation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content=req.getParameter("content");
		ANTLRInputStream antlrInputStream=new ANTLRInputStream(content);
		ReportParserLexer lexer=new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream=new CommonTokenStream(lexer);
		ReportParserParser parser=new ReportParserParser(tokenStream);
		ScriptErrorListener errorListener=new ScriptErrorListener();
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);
		parser.expression();
		List<ErrorInfo> infos=errorListener.getInfos();
		writeObjectToJson(resp, infos);
	}
	
	public void conditionScriptValidation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content=req.getParameter("content");
		ANTLRInputStream antlrInputStream=new ANTLRInputStream(content);
		ReportParserLexer lexer=new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream=new CommonTokenStream(lexer);
		ReportParserParser parser=new ReportParserParser(tokenStream);
		ScriptErrorListener errorListener=new ScriptErrorListener();
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);
		parser.expr();
		List<ErrorInfo> infos=errorListener.getInfos();
		writeObjectToJson(resp, infos);
	}
	
	
	public void parseDatasetName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String expr=req.getParameter("expr");
		ANTLRInputStream antlrInputStream=new ANTLRInputStream(expr);
		ReportParserLexer lexer=new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream=new CommonTokenStream(lexer);
		ReportParserParser parser=new ReportParserParser(tokenStream);
		parser.removeErrorListeners();
		DatasetContext ctx=parser.dataset();
		String datasetName=ctx.Identifier().getText();
		Map<String,String> result=new HashMap<String,String>();
		result.put("datasetName", datasetName);
		writeObjectToJson(resp, result);
	}
	public void savePreviewData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String content=req.getParameter("content");
		content=decodeContent(content);
		InputStream inputStream=IOUtils.toInputStream(content,"utf-8");
		ReportDefinition reportDef=reportParser.parse(inputStream,PREVIEW_KEY);
		reportRender.rebuildReportDefinition(reportDef);
		IOUtils.closeQuietly(inputStream);
		CacheUtils.cacheReportDefinition(PREVIEW_KEY, reportDef);
		try {
			CacheMessageService cacheMessageService = applicationContext.getBean(CacheMessageService.class);
			if (cacheMessageService != null) {
				//分布式系统，或微服务中，借用mq进行本地消息的同步。
				cacheMessageService.saveReportCache("p", content);
			}
		}catch (NoSuchBeanDefinitionException e){

		}
	}

	/**
	 * 设计模式加载报表文件
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void loadReport(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file=req.getParameter("file");
		if(file==null){
			throw new ReportDesignException("Report file can not be null.");
		}
		file=ReportUtils.decodeFileName(file);
		ReportDefinition obj=CacheUtils.getReportDefinition(file);
		if(obj!=null){
			ReportDefinition reportDef=obj;
			CacheUtils.removeReportDefinition(file);
			writeObjectToJson(resp, new ReportDefinitionWrapper(reportDef));
		}else{
			ReportDefinition reportDef=reportRender.parseReport(file);
			writeObjectToJson(resp, new ReportDefinitionWrapper(reportDef));			
		}
	}

	/**
	 * 删除报表
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void deleteReportFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file=req.getParameter("file");
		if(file==null){
			throw new ReportDesignException("Report file can not be null.");
		}
		ReportProvider targetReportProvider=null;
		for(ReportProvider provider:reportProviders){
			if(file.startsWith(provider.getPrefix())){
				targetReportProvider=provider;
				break;
			}
		}
		if(targetReportProvider==null){
			throw new ReportDesignException("File ["+file+"] not found available report provider.");
		}
		targetReportProvider.deleteReport(file);
		CacheUtils.removeReportDefinition(file);
		try {
			CacheMessageService cacheMessageService = applicationContext.getBean(CacheMessageService.class);
			if (cacheMessageService != null) {
				//分布式系统，或微服务中，借用mq进行本地消息的同步。
				cacheMessageService.deleteReportCache(file);
			}
		}catch (NoSuchBeanDefinitionException e){

		}
	}
	/**
	 * 下载报表
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void downloadReportFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file=req.getParameter("file");
		file=ReportUtils.decodeFileName(file);
		String content=req.getParameter("content");
		content=decodeContent(content);
		String[] files=file.split(":");
		if(files.length>1){
			file=files[1];
		}
		InputStream inputStream=IOUtils.toInputStream(content,"utf-8");
		byte[] data = IOUtils.toByteArray(inputStream);
		IOUtils.closeQuietly(inputStream);
		resp.reset();
		resp.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(file,"utf-8")+"\"");
		resp.addHeader("Content-Length", "" + data.length);
		resp.setContentType("application/octet-stream; charset=UTF-8");

		IOUtils.write(data, resp.getOutputStream());
		resp.flushBuffer();
	}
	/**
	 * 保存报表
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void saveReportFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file=req.getParameter("file");
		file=ReportUtils.decodeFileName(file);
		String content=req.getParameter("content");
		content=decodeContent(content);
		ReportProvider targetReportProvider=null;
		for(ReportProvider provider:reportProviders){
			if(file.startsWith(provider.getPrefix())){
				targetReportProvider=provider;
				break;
			}
		}
		if(targetReportProvider==null){
			throw new ReportDesignException("File ["+file+"] not found available report provider.");
		}
		targetReportProvider.saveReport(file, content);
		InputStream inputStream=IOUtils.toInputStream(content,"utf-8");
		ReportDefinition reportDef=reportParser.parse(inputStream, file);
		reportRender.rebuildReportDefinition(reportDef);
		CacheUtils.cacheReportDefinition(file, reportDef);
		IOUtils.closeQuietly(inputStream);
		try{
			CacheMessageService cacheMessageService = applicationContext.getBean(CacheMessageService.class);
			if(cacheMessageService!=null){
				//分布式系统，或微服务中，借用mq进行本地消息的同步。
				cacheMessageService.saveReportCache(file,content);

			}
		}catch (NoSuchBeanDefinitionException e){

		}

	}

	/**
	 * 加载报表存储驱动
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void loadReportProviders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		writeObjectToJson(resp, reportProviders);
	}
	
	public void setReportRender(ReportRender reportRender) {
		this.reportRender = reportRender;
	}
	
	public void setReportParser(ReportParser reportParser) {
		this.reportParser = reportParser;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		super.setApplicationContext(applicationContext);
		Collection<ReportProvider> providers=applicationContext.getBeansOfType(ReportProvider.class).values();
		for(ReportProvider provider:providers){
			if(provider.disabled() || provider.getName()==null){
				continue;
			}
			reportProviders.add(provider);
		}
	}

	@Override
	public String url() {
		return "/designer";
	}
}
