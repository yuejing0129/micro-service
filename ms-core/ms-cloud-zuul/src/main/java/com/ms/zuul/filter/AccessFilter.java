package com.ms.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ms.zuul.filter.core.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.system.auth.AuthUtil;
import com.system.auth.model.AuthClient;
import com.system.comm.utils.FrameJsonUtil;
import com.system.handle.model.ResponseCode;
import com.system.handle.model.ResponseFrame;

public class AccessFilter extends ZuulFilter {

	private static Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);

	@Override
	public String filterType() {
		//pre、post和error。分别代表前置过滤，后置过滤和异常过滤
		return "pre";
	}
	@Override
	public int filterOrder() {
		//过滤器顺序
		return 0;
	}
	@Override
	public boolean shouldFilter() {
		//代表这个过滤器是否生效
		return true;
	}
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		String clientId = request.getParameter("clientId");
		String sign = request.getParameter("sign");
		String time = request.getParameter("time");

		LOGGER.info("{ clientId:" + clientId + ", time:" + time + ", sign:" + sign + " } 请求地址: " + request.getRequestURI());
		AuthClient client = AuthUtil.get(clientId);
		if(client == null) {
			LOGGER.error("非法请求(abnormal signature): { clientId:" + clientId + ", time:" + time + ", sign:" + sign + " } 请求地址: " + request.getRequestURI());
			ResponseFrame frame = new ResponseFrame();
			frame.setCode(ResponseCode.ABNORMAL_SIGNATURE.getCode());
			frame.setMessage(ResponseCode.ABNORMAL_SIGNATURE.getMessage());
			ctx.getResponse().setContentType("text/html;charset=UTF-8");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			ctx.setResponseBody(FrameJsonUtil.toString(frame));
			return null;
		}
		//处理是否超额访问
		if(client.getReqMaxSecond() != null && client.getReqMaxSecond().longValue() > 0) {
			RateLimiter rateLimiter = new RateLimiter(clientId);
			if(!rateLimiter.acquireSecond(client.getReqMaxSecond().longValue())) {
				LOGGER.error("超过每秒钟最大请求数: " + client.getReqMaxSecond() + "次");
				ResponseFrame frame = new ResponseFrame();
				frame.setCode(ResponseCode.REQ_MAX_SECOND.getCode());
				frame.setMessage(ResponseCode.REQ_MAX_SECOND.getMessage() + ": " + client.getReqMaxSecond() + "次");
				ctx.getResponse().setContentType("text/html;charset=UTF-8");
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(401);
				ctx.setResponseBody(FrameJsonUtil.toString(frame));
				return null;
			}
		}
		if(client.getReqMaxHour() != null && client.getReqMaxHour().longValue() > 0) {
			RateLimiter rateLimiter = new RateLimiter(clientId);
			if(!rateLimiter.acquireHour(client.getReqMaxHour().longValue())) {
				LOGGER.error("超过每小时最大请求数: " + client.getReqMaxHour() + "次");
				ResponseFrame frame = new ResponseFrame();
				frame.setCode(ResponseCode.REQ_MAX_HOUR.getCode());
				frame.setMessage(ResponseCode.REQ_MAX_HOUR.getMessage() + ": " + client.getReqMaxHour() + "次");
				ctx.getResponse().setContentType("text/html;charset=UTF-8");
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(401);
				ctx.setResponseBody(FrameJsonUtil.toString(frame));
				return null;
			}
		}
		if(!AuthUtil.authVerify(clientId, time, sign)) {
			LOGGER.error("非法请求(abnormal signature): { clientId:" + clientId + ", time:" + time + ", sign:" + sign + " } 请求地址: " + request.getRequestURI());
			ResponseFrame frame = new ResponseFrame();
			frame.setCode(ResponseCode.ABNORMAL_SIGNATURE.getCode());
			frame.setMessage(ResponseCode.ABNORMAL_SIGNATURE.getMessage());
			ctx.getResponse().setContentType("text/html;charset=UTF-8");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			ctx.setResponseBody(FrameJsonUtil.toString(frame));
			return null;
		}
		//判断请求地址是否通过
		if(!AuthUtil.authVerifyReqUrl(clientId, request.getRequestURI())) {
			LOGGER.error("地址: " + request.getRequestURI() + " 没有权限, clientId[" + clientId + "]");
			ResponseFrame frame = new ResponseFrame();
			frame.setCode(ResponseCode.ABNORMAL_SIGNATURE.getCode());
			frame.setMessage(ResponseCode.ABNORMAL_SIGNATURE.getMessage());
			ctx.getResponse().setContentType("text/html;charset=UTF-8");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			ctx.setResponseBody(FrameJsonUtil.toString(frame));
			return null;
		}
		//这里return的值没有意义，zuul框架没有使用该返回值
		return null;
	}
}