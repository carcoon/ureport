package com.hcharger.report.provider;

import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;

import com.bstek.ureport.domain.UreportFile;
import com.bstek.ureport.report.service.ReportService;
import com.bstek.ureport.vo.Result;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Mysql 报表存储
 * @author ssc
 * @version 2018年5月9日
 *
 */


@Component
@ConfigurationProperties(prefix = "ureport.mysql.provider")
public class MySQLProvider implements ReportProvider{
	private static final String NAME = "mysql-provider";
	
	private String prefix = "mysql:";
	
	private boolean disabled;
//
//	@Autowired
//	private UreportFileRepo ureportFileRepo;
	@Resource
	private ReportService reportService;
	@Override
	public InputStream loadReport(String file) {
		Result<UreportFile> result = reportService.findTopByName(getCorrectName(file));
		UreportFile ureportFileEntity = result.getData();
		byte[] content = ureportFileEntity.getContent();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
		return inputStream;
	}

	@Override
	public void deleteReport(String file) {
        Result<UreportFile> result = reportService.findTopByName(getCorrectName(file));
        UreportFile ureportFileEntity = result.getData();
        reportService.deleteUreportFile(ureportFileEntity);
	}

	@Override
	public List<ReportFile> getReportFiles() {
	    Result<List<UreportFile>> result = reportService.findAll();
		List<UreportFile> list = result.getData();
		List<ReportFile> reportList = new ArrayList<>();
		for (UreportFile ureportFileEntity : list) {
			reportList.add(new ReportFile(ureportFileEntity.getName(), ureportFileEntity.getUpdateTime()));
		}
		return reportList	;
	}

	@Override
	public void saveReport(String file, String content) {
		file = getCorrectName(file);
        Result<UreportFile> result = reportService.findTopByName(getCorrectName(file));
        UreportFile ureportFileEntity = result.getData();
		Date currentDate = new Date();
		try {

			if (ureportFileEntity == null) {
				ureportFileEntity = new UreportFile();
				ureportFileEntity.setName(file);
				ureportFileEntity.setContent(content.getBytes("utf-8"));
				//报表初始化为
				ureportFileEntity.setStatus((byte)0);
				ureportFileEntity.setCreateTime(currentDate);
				ureportFileEntity.setUpdateTime(currentDate);
                reportService.save(ureportFileEntity);
			} else {
				ureportFileEntity.setContent(content.getBytes("utf-8"));
				ureportFileEntity.setUpdateTime(currentDate);
                reportService.save(ureportFileEntity);
			}
		}catch (UnsupportedEncodingException e){

		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean disabled() {
		return disabled;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	/**
	 * 获取没有前缀的文件名
	 * @param name
	 * @return
	 */
	private String getCorrectName(String name){
		if(name.startsWith(prefix)){
			name = name.substring(prefix.length(), name.length());
		}
		return name; 
	}
}
