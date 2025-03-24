package com.gptsam.core.env;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "local")
public abstract class EnvLocal {
	// 이 클래스를 상속받은 클래스는 application-local.yml 프로파일을 사용
}
