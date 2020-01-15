package com.bstek.ureport.report.service;



import com.bstek.ureport.domain.UreportFile;

import com.bstek.ureport.report.repository.UreportFileRepo;
import com.bstek.ureport.vo.PageList;
import com.bstek.ureport.vo.Result;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author lqf
 */
@RestController
@Component
public class ReportServiceImpl implements ReportService {
    private static Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);


    @Autowired
    private UreportFileRepo ureportFileRepo;





    @Override
    public Result<PageList<UreportFile>> getReportPageList(String keyword, int page, int size) {
        Result result = Result.error();

        keyword = "%" + keyword + "%";
        Pageable pageable=new PageRequest(page, size, Sort.Direction.DESC, "updateTime");

        Page<UreportFile> ureportFiles = ureportFileRepo.findAllByNameLikeOrTitleLike(keyword,keyword,pageable);
        return Result.success(new PageList<UreportFile>(ureportFiles.getContent(),ureportFiles.getTotalElements(),page,size));

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteReport(long reportId, String operateUserId) {
        Result result = Result.error();

        UreportFile ureportFile = ureportFileRepo.findOne(reportId);
        if(ureportFile == null){
            result.setMessage("报表不存在:"+reportId);
            return  result;
        }


        result = Result.success();
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateStatus(long reportId, byte status, String operateUserId) {
        Result result = Result.error();

        UreportFile ureportFile = ureportFileRepo.findOne(reportId);
        if(ureportFile == null){
            result.setMessage("报表不存在:"+reportId);
            return  result;
        }

        ureportFile.setStatus(status);
        ureportFileRepo.saveAndFlush(ureportFile);

        result = Result.success();
        return result;
    }



    @Override
    public Result importReport(String filename, String title, boolean overrimp, String remark, MultipartFile file) throws Exception {

        if (!filename.matches("^.+\\.(?i)(xml)$")) {
            return  Result.error("上传文件格式不正确");
        }
        //判断文件名是否存在，
        UreportFile ureportFile=ureportFileRepo.findTopByName(filename);
        if(ureportFile!=null && !overrimp){
            return  Result.error("报表文件已经存在，请重新命名。");
        }
        Date currentDate = new Date();
        if(ureportFile==null){
            ureportFile = new UreportFile();
            ureportFile.setName(filename);
            ureportFile.setTitle(title);
//            ureportFile.setRemark(remark);
            ureportFile.setContent(file.getBytes());
            ureportFile.setCreateTime(currentDate);
            ureportFile.setUpdateTime(currentDate);
        }else {
            ureportFile.setTitle(title);
//            ureportFile.setRemark(remark);
            ureportFile.setContent(file.getBytes());
            ureportFile.setUpdateTime(currentDate);
        }

        ureportFileRepo.save(ureportFile);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<UreportFile> updateReport(UreportFile ureportFile) {
        UreportFile report=ureportFileRepo.findOne(ureportFile.getId());
        report.setTitle(ureportFile.getTitle());
//        report.setRemark(ureportFile.getRemark());
        report.setUpdateTime(new Date());
        UreportFile newUr = ureportFileRepo.save(report);
        return Result.success(newUr);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result DeleteReport(Long id) {
//        UreportFile ureportFile = ureportFileRepo.getOne(id);
//        if(ureportFile.getMenuId()!=null){
//            menuService.deleteMenu(ureportFile.getMenuId());
//        }
//        ureportFileRepo.delete(id);
        return Result.success();
    }

    @Override
    public Result<byte[]> exportReport(List<String> idList) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String id : idList) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(id)) {
                UreportFile ureportFile = ureportFileRepo.findOne(Long.parseLong(id));
                zipReportFile(ureportFile, zip);
            }
        }
        IOUtils.closeQuietly(zip);
        return Result.success(outputStream.toByteArray());
    }
    public void zipReportFile(UreportFile ureportFile, ZipOutputStream zip) {
        try {
            ZipEntry zipEntry = new ZipEntry(ureportFile.getName());
            byte[] data = ureportFile.getContent();
            zip.putNextEntry(zipEntry);
            zip.write(data);
            zip.flush();
            zip.closeEntry();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("压缩报表失败：" + ureportFile.getName(), e);
        }
    }


    @Override
    public Result<UreportFile> findTopByName(String fileName) {
        UreportFile ureportFile = ureportFileRepo.findTopByName(fileName);

        return Result.success(ureportFile);
    }

    @Override
    public Result deleteUreportFile(UreportFile ureportFile) {
        ureportFileRepo.delete(ureportFile);
        return Result.success();
    }

    @Override
    public Result<List<UreportFile>> findAll() {
        List<UreportFile> list = ureportFileRepo.findAll();
        return Result.success(list);
    }

    @Override
    public Result save(@RequestBody UreportFile ureportFile) {
        ureportFileRepo.save(ureportFile);
        return Result.success();
    }


}
