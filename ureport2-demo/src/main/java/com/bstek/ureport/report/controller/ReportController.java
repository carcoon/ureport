package com.bstek.ureport.report.controller;

import com.bstek.ureport.domain.UreportFile;
import com.bstek.ureport.report.service.ReportService;
import com.bstek.ureport.vo.PageList;
import com.bstek.ureport.vo.Result;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lqf
 */
@RestController

public class ReportController {
    private static Logger logger = LoggerFactory.getLogger(ReportController.class);
    @Resource
    private ReportService reportService;



    @GetMapping("report/getReportPageList")
    public Result getReportPageList(String token,String keyword, int page, int size){
        Result result = Result.error();
        try{
            page=page-1;
            if(page < 0){
                page = 0;
            }
            if(size<=0){
                size=10;
            }
            result = reportService.getReportPageList(keyword, page, size);
        }catch (Exception e){
            result.setMessage(e.getMessage());
            logger.info("getReportPageList:"+e.getMessage());
        }

        return result;
    }

    @PostMapping("report/deleteReport")
    public Result deleteReport(String token,long reportId){
        Result result = Result.error();
        try{
            result = reportService.deleteReport(reportId,token);
        }catch (Exception e){
            result.setMessage(e.getMessage());
            logger.info("deleteReport:"+e.getMessage());
        }

        return result;
    }


    @PostMapping("report/updateStatus")
    public Result updateStatus(String token,long reportId, byte status){
        Result result = Result.error();
        try{
            result = reportService.updateStatus(reportId, status, token);
        }catch (Exception e){
            result.setMessage(e.getMessage());
            logger.info("deleteReport:"+e.getMessage());
        }

        return result;
    }


    @PostMapping("import")
    public Result importReport(String token ,String title,Boolean overrimp,String remark,@RequestParam("file") MultipartFile file) {
        String filename = file.getOriginalFilename();
        try {
            reportService.importReport(filename, title, overrimp, remark, file);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }
    @PostMapping("update")
    public Result<UreportFile> updateReport(String token,UreportFile ureportFile){
        try{
            return reportService.updateReport(ureportFile);
        }catch (Exception e){
            return Result.error(e.getMessage());
        }

    }
    @PostMapping("delete")
    public Result deleteReport(String token,Long id){
        try{
            reportService.DeleteReport(id);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }

    }

    @PostMapping("export")
    public void exportReport(String ids,HttpServletResponse response) throws IOException {
        String[] idArr = ids.split(",");
        List<String> idList =new ArrayList<>();
        if(idArr.length>0){
            idList = Arrays.asList(idArr);
        }
        byte[] data = reportService.exportReport(idList).getData();

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"ureport.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
        response.flushBuffer();
    }
}
