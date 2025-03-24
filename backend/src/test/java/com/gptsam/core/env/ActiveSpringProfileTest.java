package com.gptsam.core.env;

import com.gptsam.core.env.resolver.DynamicProfileResolver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(resolver = DynamicProfileResolver.class)
public abstract class ActiveSpringProfileTest {
	// 이 클래스를 상속받은 클래스는 현재 활성화된 스프링 프로파일을 사용
}
