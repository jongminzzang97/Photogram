package com.cos.photogramstart.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

// 어노테이션이 없어도 JpaRepository를 상속하면 Ioc에 자동으로 등록이 된다.
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// JPA Query Creation from method names
	User findByUsername(String username);
	
}
