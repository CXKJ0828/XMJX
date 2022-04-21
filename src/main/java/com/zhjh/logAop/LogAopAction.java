package com.zhjh.logAop;

/**
 * Created by 88888888 on 2020/7/31.
 */

import com.gexin.fastjson.JSON;
import com.zhjh.annotation.SystemLog;
import com.zhjh.entity.LogFormMap;
import com.zhjh.mapper.LogMapper;
import com.zhjh.util.Common;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 草帽boy on 2017/2/21.
 */
@Aspect
public class LogAopAction {
	//本地异常日志记录对象
	private  static  final Logger logger = LoggerFactory.getLogger(LogAopAction. class);
	@Inject
	private LogMapper logMapper;


	@Pointcut("execution(* com.zhjh.controller..*.*(..))")
	private void controllerAspect(){}

	/**
	 * 方法开始执行
	 */
	@Before("controllerAspect()")
	public void doBefore(){
		System.out.println("开始");
	}

	/**
	 * 方法结束执行
	 */
	@After("controllerAspect()")
	public void after(){
		System.out.println("结束");
	}

	/**
	 * 方法结束执行后的操作
	 */
	@AfterReturning("controllerAspect()")
	public void doAfter(){


		System.out.println(">>>>>>>>doAfter");
	}

	/**
	 * 方法有异常时的操作
	 */
	@AfterThrowing("controllerAspect()")
	public void doAfterThrow(){
		System.out.println("例外通知-----------------------------------");
	}

	/**
	 * 方法执行
	 * @param point
	 * @return
	 * @throws Throwable
	 */
	@Around("controllerAspect()")
	public Object around(ProceedingJoinPoint point) throws Throwable{
		Object result = null;
		// 执行方法名
		String methodName = point.getSignature().getName();
		String className = point.getTarget().getClass().getSimpleName();
		LogFormMap logForm = new LogFormMap();
		Map<String, Object> map = null;
		String user = null;
		Long start = 0L;
		Long end = 0L;
		Long time = 0L;
		String ip = null;
		try {
			ip = SecurityUtils.getSubject().getSession().getHost();
		} catch (Exception e) {
			ip = "无法获取登录用户Ip";
		}
		try {
			// 登录名
			user = SecurityUtils.getSubject().getPrincipal().toString();
			if (Common.isEmpty(user)) {
				user = "无法获取登录用户信息！";
			}
		} catch (Exception e) {
			user = "无法获取登录用户信息！";
		}
		// 当前用户
		try {
			result = point.proceed();
			map=getControllerMethodDescription(point);
			// 执行方法所消耗的时间
			start = System.currentTimeMillis();
			end = System.currentTimeMillis();
			time = end - start;
		} catch (Throwable e) {
			return result;
		}
		try {
			logForm.put("accountName",user);
			logForm.put("module",map.get("module"));
			logForm.put("methods",map.get("methods"));
			logForm.put("reqParam",map.get("reqParam"));
			logForm.put("description",map.get("description"));
			logForm.put("actionTime",time.toString());
			logForm.put("userIP",ip);
			logMapper.addEntity(logForm);
		}  catch (Exception e) {
			//记录本地异常日志
			logger.error("异常信息:{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 *
	 * @param joinPoint 切点
	 * @return 方法描述
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					map.put("module", method.getAnnotation(SystemLog.class).module());
					map.put("methods", method.getAnnotation(SystemLog.class).methods());
					String de = method.getAnnotation(SystemLog.class).description();
					if(Common.isEmpty(de))de="执行成功!";
					map.put("description", de);
					break;
				}
			}
		}
		String reqParam = preHandle(joinPoint);
		map.put("reqParam", reqParam);
		return map;
	}

	/**
	 * 入参数据
	 * @param joinPoint
	 * @return
	 */
	private String preHandle(JoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String reqParam = "";
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method targetMethod = methodSignature.getMethod();
		Annotation[] annotations = targetMethod.getAnnotations();
		for (Annotation annotation : annotations) {
			//此处可以改成自定义的注解
			if (annotation.annotationType().equals(RequestMapping.class)) {
				reqParam = JSON.toJSONString(request.getParameterMap());
				break;
			}
		}
		return reqParam;
	}

}