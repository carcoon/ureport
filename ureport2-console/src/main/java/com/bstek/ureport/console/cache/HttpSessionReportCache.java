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
package com.bstek.ureport.console.cache;

import java.util.*;
import java.util.concurrent.*;

import javax.servlet.http.HttpServletRequest;

import com.bstek.ureport.Utils;
import com.bstek.ureport.cache.CacheObject;
import com.bstek.ureport.cache.DefaultMemoryReportCache;
import com.bstek.ureport.cache.ReportCache;
import com.bstek.ureport.chart.ChartData;
import com.bstek.ureport.console.RequestHolder;
import com.bstek.ureport.definition.ReportDefinition;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

import static com.bstek.ureport.console.ServletAction.PREVIEW_KEY;

/**
 * @author Jacky.gao
 * @since 2017年3月8日
 */
public class HttpSessionReportCache implements ReportCache {
	protected static Logger logger = LoggerFactory.getLogger(HttpSessionReportCache.class);

	private Map<String,ObjectMap> sessionReportMap=new HashMap<String,ObjectMap>();
	private Map<String,ReportDefinition> reportMap=new ConcurrentHashMap<String,ReportDefinition>();
	private boolean disabled;
	private boolean disabledReportDefinition;
	public ObjectMap getSessionMap(){
		HttpServletRequest req=RequestHolder.getRequest();
		ObjectMap objectMap=getObjectMap(req);
		return  objectMap;
	}
	public boolean isTempFile(String file) {
		if("classpath:template/template.ureport.xml".equals(file) || "p".equals(file)){
			return true;
		}
		return false;
	}

	@Override
	public ReportDefinition getReportDefinition(String file) {
		if(isTempFile(file)){
			return (ReportDefinition)getSessionMap().get(file);
		}

		if(disabledReportDefinition ){
			return null;
		}
		return reportMap.get(file);
	}
	@Override
	public void cacheReportDefinition(String file,ReportDefinition reportDefinition) {
		if(isTempFile(file)){
			getSessionMap().put(file,reportDefinition);
			return;
		}
		if(disabledReportDefinition &&!isTempFile(file)){
			return;
		}
		if(reportMap.containsKey(file)){
			reportMap.remove(file);
		}
		reportMap.put(file, reportDefinition);
	}

	@Override
	public void removeReportDefinition(String file) {
		if(isTempFile(file)){
			getSessionMap().remove(file);
			return;
		}
		if(disabledReportDefinition){
			return;
		}
		if(reportMap.containsKey(file)){
			reportMap.remove(file);
		}
	}

	@Override
	public ChartData getChartData(String chartId) {
		HttpServletRequest req=RequestHolder.getRequest();
		ObjectMap objectMap=getObjectMap(req);
		ChartData chartData=(ChartData)objectMap.get(chartId);
		return chartData;
	}

	@Override
	public void storeChartData(String chartId, ChartData chartData) {
		HttpServletRequest req=RequestHolder.getRequest();
		ObjectMap objectMap=getObjectMap(req);
		objectMap.put(chartId,chartData);
	}

	@Override
	public void removeChartData(String chartId) {
		HttpServletRequest req=RequestHolder.getRequest();
		ObjectMap objectMap=getObjectMap(req);
		objectMap.remove(chartId);
	}

	@Override
	public void doWatching() {
		//开启定时器，监控超时缓存清理
		ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
		//Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				Utils.logToConsole("run doWatching"+ System.currentTimeMillis());
				Iterator<Map.Entry<String, ObjectMap>> it = sessionReportMap.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, ObjectMap> entry = it.next();
					if(entry.getValue().isExpired()){
						Utils.logToConsole(entry.getKey()+" removed ");
						it.remove();
					}
				}

			}
		}, 30000, 30000, TimeUnit.MILLISECONDS);
	}


	
	@Override
	public boolean disabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	@Override
	public boolean disabledReportDefinition() {
		return disabledReportDefinition;
	}

	public void setDisabledReportDefinition(boolean disabledReportDefinition) {
		this.disabledReportDefinition = disabledReportDefinition;
	}

	private ObjectMap getObjectMap(HttpServletRequest req) {
		List<String> expiredList=new ArrayList<String>();
		for(String key:sessionReportMap.keySet()){
			ObjectMap reportObj=sessionReportMap.get(key);
			if(reportObj.isExpired()){
				expiredList.add(key);
			}
		}
		for(String key:expiredList){
			sessionReportMap.remove(key);
		}
		String sessionId=req.getSession().getId();
		ObjectMap obj=sessionReportMap.get(sessionId);
		if(obj!=null){
			return obj;
		}else{
			ObjectMap objMap=new ObjectMap();
			sessionReportMap.put(sessionId, objMap);
			return objMap;
		}
	}
}
