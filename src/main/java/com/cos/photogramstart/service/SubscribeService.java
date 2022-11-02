package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
	
	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; // Repository는 EntityManager를 구현해서 만들어져 있는 구현체
	
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> 구독리스트(int principalId, int pageUserId) {
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profileImageUrl, ");
		sb.append("if ((SELECT 1 FROM subscribe WHERE fromUserId=? AND toUserId=u.id), 1, 0) subscribeState, ");
		sb.append("if ((?=u.id), 1, 0) equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserID ");
		sb.append("WHERE s.fromUserId = ? ");
		
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
		
		return subscribeDtos;
	}

	@Transactional
	public void 구독하기(int fromUserId, int toUserId) {
		
		// UserRepository 에서 findbyId를 진행해서 User 객체들을 찾아서
		// subscribeRepository 에 save를 하는 방법도 있지만 이것이 더 불편하다.
		// native query를 사용하는 것이 더 나은 방법이다.
		// exception은 try catch 로 처리해주는 것이 좋다.
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		}
		catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		subscribeRepository.mUnsubscribe(fromUserId, toUserId);
	}
	
}
