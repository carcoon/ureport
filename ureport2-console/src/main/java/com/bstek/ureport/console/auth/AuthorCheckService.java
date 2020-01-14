package com.bstek.ureport.console.auth;

import javax.servlet.http.HttpServletRequest;

public interface AuthorCheckService {
	boolean authorValidate(HttpServletRequest req);
}
