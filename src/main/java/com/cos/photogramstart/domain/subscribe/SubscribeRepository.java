package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>{

	@Modifying //INSERT, DELETE, UPDATE를 네이티브 쿼리로 작성하려면 해당 어노테이션이 필요하다.
	@Query(value = "INSERT INTO subscribe(fromUserID, toUserID, createDate) VALUES(:fromUserId, :toUserId, now())", nativeQuery = true)
	void mSubscribe(int fromUserId, int toUserId); // return (변경된 행의 개수)
	
	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE fromUserID = :fromUserId AND  toUserID =:toUserId", nativeQuery = true)
	void mUnsubscribe(int fromUserId, int toUserId);
	
	@Query(value="SELECT COUNT(*) FROM subscribe WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
	int mSubscripbeState(int principalId, int pageUserId);
	
	// 팔로잉 수
	@Query(value="SELECT COUNT(*) FROM subscribe WHERE fromUserId = :pageUserId", nativeQuery = true)
	int mfollowingCount(int pageUserId);
	
	// 팔로워 수 
	@Query(value="SELECT COUNT(*) FROM subscribe WHERE toUserId = :pageUserId", nativeQuery = true)
	int mfollowerCount(int pageUserId);
}
