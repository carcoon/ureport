package com.bstek.ureport.console.cache;

public interface CacheMessageService {
	// 设计报表保存时，清除其他节点缓存
	void deleteReportCache(String file);
	void saveReportCache(String file,String content);

}
