package com.gptsam.core.user.domain;

import lombok.Data;

@Data
public class User {

	Long id;

	String nickname;

	public User(Long id, String nickname) {
		this.id = id;
		this.nickname = nickname;
	}

}
