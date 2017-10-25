package com.module.admin.log.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.module.admin.BaseController;
import com.module.admin.log.utils.LogUtil;
import com.system.handle.model.ResponseCode;
import com.system.handle.model.ResponseFrame;

/**
 * prj_api的Controller
 * @author yuejing
 * @date 2016-11-30 13:30:00
 * @version V1.0.0
 */
@Controller
public class LogInfoController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogInfoController.class);

	/**
	 * 跳转到管理页
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logInfo/f-view/manager")
	public String manger(HttpServletRequest request, ModelMap modelMap) {
		return "admin/log/info-manager";
	}

	/**
	 * 获取信息
	 * @return
	 */
	@RequestMapping(value = "/logInfo/f-json/find")
	@ResponseBody
	public void pageQuery(HttpServletRequest request, HttpServletResponse response
			) {
		Map<String, Object> paramsMap = getParamsMap(request);
		ResponseFrame frame = null;
		try {
			frame = LogUtil.post("/api/log/find", paramsMap);
		} catch (Exception e) {
			LOGGER.error("分页获取信息异常: " + e.getMessage(), e);
			frame = new ResponseFrame(ResponseCode.FAIL);
		}
		writerJson(response, frame);
	}

}