package com.bstek.ureport.auth;

import com.bstek.ureport.console.auth.AuthorCheckService;
import com.bstek.ureport.report.service.ReportServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@Service
public class AuthorCheckServiceImpl implements AuthorCheckService {
	private static Logger logger = LoggerFactory.getLogger(AuthorCheckServiceImpl.class);


	@Override
	public boolean authorValidate(HttpServletRequest req) {
		String file=req.getParameter("_u");
		String token = req.getParameter("token");

		logger.info("file:{},token:{}",file,token);
		return true;
	}
}
