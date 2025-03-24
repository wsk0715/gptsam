package com.gptsam.core.env.resolver;

import org.springframework.test.context.ActiveProfilesResolver;

/**
 * 테스트 시 현재 활성화 된 스프링 프로파일을 동적으로 제공하는 리졸버
 */
public class DynamicProfileResolver implements ActiveProfilesResolver {

	@Override
	public String[] resolve(Class<?> targetClass) {
		String activeProfile = System.getProperty("spring.profiles.active");
		return new String[]{activeProfile};
	}

}
