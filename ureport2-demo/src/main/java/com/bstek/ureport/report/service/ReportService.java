package com.bstek.ureport.report.service;

import com.bstek.ureport.domain.UreportFile;
import com.bstek.ureport.vo.PageList;
import com.bstek.ureport.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lqf
 */

public interface ReportService {


    /**
     *获取报表列表
     */

    public Result<PageList<UreportFile>> getReportPageList(String keyword,
																  int page,
																  int size);

    /**
     *删除报表
     */

    public Result deleteReport(long reportId, String operateUserId);

    /**
     *更新报表状态
     */

    public Result updateStatus(long reportId, byte status, String operateUserId);



    public Result importReport(String filename, String title,
							   boolean overrimp, String remark,
							   MultipartFile file)throws Exception;



    public Result<UreportFile> updateReport(UreportFile ureportFile);

    public Result DeleteReport(Long id);

    public Result<byte[]> exportReport(List<String> idList);



    public Result<UreportFile> findTopByName(String fileName);

    public Result deleteUreportFile(UreportFile ureportFile);

    public Result<List<UreportFile>> findAll();

    public Result save(UreportFile ureportFile);
}
