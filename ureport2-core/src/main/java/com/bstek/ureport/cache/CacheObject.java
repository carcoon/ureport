package com.bstek.ureport.cache;

public class CacheObject<T> {
	private long start;
	private static final int MILLISECOND=300000;//default expired time is 5 minutes.
	private T data;
	public T getData() {
		return data;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setData(T data) {
		this.data = data;
	}
	public static<T> CacheObject<T> cacheObject(T data) {
		CacheObject co = new CacheObject();

	    co.setStart(System.currentTimeMillis());
	    co.setData(data);

		return co;
	}
	public boolean isExpired(){
		long diff=System.currentTimeMillis()-start;
		if(diff>=MILLISECOND){
			return true;
		}
		return false;
	}

}
