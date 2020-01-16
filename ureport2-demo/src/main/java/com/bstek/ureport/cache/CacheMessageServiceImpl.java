package com.bstek.ureport.cache;

import com.bstek.ureport.console.cache.CacheMessageService;

public class CacheMessageServiceImpl implements CacheMessageService {
	@Override
	public void deleteReportCache(String file) {
		// 通过MQ消息广播
	}

	@Override
	public void saveReportCache(String file, String content) {

	}
}
