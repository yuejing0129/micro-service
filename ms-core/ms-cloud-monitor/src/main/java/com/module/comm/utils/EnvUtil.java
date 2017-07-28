package com.module.comm.utils;

import org.springframework.core.env.Environment;

import com.module.comm.enums.Env;
import com.system.comm.utils.FrameSpringBeanUtil;

public class EnvUtil {

	/**
	 * 获取属性的值
	 * @param env
	 * @return
	 */
	public static String get(Env env) {
		Environment environment = FrameSpringBeanUtil.getBean(Environment.class);
		return environment.getProperty(env.getCode());
	}
}
