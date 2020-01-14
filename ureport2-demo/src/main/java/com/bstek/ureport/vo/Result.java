package com.bstek.ureport.vo;

import com.google.gson.Gson;
import com.bstek.ureport.exception.Errorcode;

//import org.springframework.data.domain.Page;


public class Result<T>{
    // 状态码
    protected  int errorcode;
    // 信息
    protected  String message;
    /*具体的内容*/
    private T data;
    public Result(){
        this.data=null;
    }
    public Result(T data){
        this.data=data;
    }
    public void setResultMsg(int errorcode,String message){
        this.errorcode = errorcode;
        this.message = message;
    }
    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Result success() {
        Result result = Result.success(Errorcode.SUCCESS_MSG);
        return result;
    }
    public static Result success(String msg) {
        Result result = new Result();
        result.setErrorcode(Errorcode.SUCCESS);
        result.setMessage(msg);
        //result.setData(object);
        return result;
    }
    public static<T> Result<T> success(T data) {
        Result<T> result = new Result(data);
        result.setErrorcode(Errorcode.SUCCESS);
        result.setMessage(Errorcode.SUCCESS_MSG);
//        result.setData(data);
        return result;
    }
//    public static<T> Result<PageList<T>> success(Page<T> data,int page,int size) {
//        PageList<T> rest = new PageList<T>(data.getContent(),data.getTotalElements(),page,size);
//        Result<PageList<T>> result = new Result(rest);
//        result.setErrorcode(Errorcode.SUCCESS);
//        result.setMessage(Errorcode.SUCCESS_MSG);
//        return result;
//    }



    public static<T> Result<T> success(String msg, T data) {
        Result<T> result = new Result(data);
        result.setErrorcode(Errorcode.SUCCESS);
        result.setMessage(msg);
//        result.setData(data);
        return result;
    }


    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setErrorcode(code);
        result.setMessage(msg);
        result.setData(null);
        return result;
    }
    public static Result error(String msg) {
        Result result = Result.error(Errorcode.FAILED,msg);
        return result;
    }
    public static Result error() {
        return Result.error(Errorcode.FAILED,Errorcode.FAILED_MSG);
    }
    public static Result hystrixError(Class clz) {
        return Result.error(Errorcode.INTERNAL_ERROR,"微服务调用失败:"+clz.getName());
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}