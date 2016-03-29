package qdh.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import qdh.controller.ControllerConfig;
import qdh.pageModel.SessionInfo;
import qdh.sysParms.DefaultFunction;



public class SecurityInterceptor implements HandlerInterceptor {
	private final String MOBILE ="/Mobile";
	private static final Logger logger = Logger.getLogger(SecurityInterceptor.class);


	/**
	 * 完成页面的render后调用
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {

	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在调用controller具体方法前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		logger.info(url);

		if (DefaultFunction.isDefaultFunction(url)) {// 如果要访问的资源是不需要验证的
			return true;
		}

		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(ControllerConfig.HQ_SESSION_INFO);
		if (sessionInfo == null || sessionInfo.getUserId() == 0) {// 如果没有登录或登录超时
			request.setAttribute("msg", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
			if (url.indexOf(MOBILE) == -1)
				request.getRequestDispatcher("/indexh.jsp").forward(request, response);
			else 
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			return false;
		}

//		if (!sessionInfo.getResourceList().contains(url)) {// 如果当前用户没有访问此资源的权限
//			request.setAttribute("msg", "您没有访问此资源的权限！<br/>请联系超管赋予您<br/>[" + url + "]<br/>的资源访问权限！");
//			request.getRequestDispatcher("/error/noSecurity.jsp").forward(request, response);
//			return false;
//		}

		return true;
	}
}
