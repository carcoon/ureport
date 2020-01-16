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
package com.bstek.ureport.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.bstek.ureport.chart.ChartData;
import com.bstek.ureport.definition.ReportDefinition;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

/**
 * @author Jacky.gao
 * @since 2016年12月4日
 */
public class DefaultMemoryReportCache implements ReportCache {
	protected static Logger logger = LoggerFactory.getLogger(DefaultMemoryReportCache.class);

	private Map<String,ReportDefinition> reportMap=new ConcurrentHashMap<String,ReportDefinition>();
	private Map<String, CacheObject> cacheObjectMap = new ConcurrentHashMap<String,CacheObject>();


	@Override
	public ChartData getChartData(String chartId) {
		CacheObject<ChartData> cacheObject=cacheObjectMap.get(chartId);
		if(cacheObject!=null){
			return cacheObject.getData();
		}
		return null;
	}



	@Override
	public void storeChartData(String chartId, ChartData chartData) {
		CacheObject<ChartData> cacheObject=cacheObjectMap.get(chartId);
		if(cacheObject!=null){
			cacheObject.setStart(System.currentTimeMillis());
			cacheObject.setData(chartData);
		}else {
			cacheObject = CacheObject.cacheObject(chartData);
			cacheObjectMap.put(chartId, cacheObject);
		}

	}

	@Override
	public void removeChartData(String chartId) {
		if(cacheObjectMap.containsKey(chartId)) {
			cacheObjectMap.remove(chartId);
		}
	}

	@Override
	public void doWatching() {
		//开启定时器，监控超时缓存清理
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				logger.info("run doWatching"+ System.currentTimeMillis());
				Iterator<Map.Entry<String, CacheObject>> it = cacheObjectMap.entrySet().iterator();
				while(it.hasNext()){
					 Map.Entry<String, CacheObject> entry = it.next();
					 if(entry.getValue().isExpired()){
					 	logger.info(entry.getKey()+" removed ");
					 	it.remove();
					 }
 				}

			}
		}, 10000, 5000, TimeUnit.MILLISECONDS);

	}

	@Override
	public boolean disabled() {
		return false;
	}

	@Override
	public boolean disabledReportDefinition() {
		return false;
	}

	@Override
	public ReportDefinition getReportDefinition(String file) {
		return reportMap.get(file);
	}
	@Override
	public void cacheReportDefinition(String file,ReportDefinition reportDefinition) {
		if(reportMap.containsKey(file)){
			reportMap.remove(file);
		}
		reportMap.put(file, reportDefinition);
	}

	@Override
	public void removeReportDefinition(String file) {
		if(reportMap.containsKey(file)){
			reportMap.remove(file);
		}
	}
}
