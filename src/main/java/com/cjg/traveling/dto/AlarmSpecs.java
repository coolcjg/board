package com.cjg.traveling.dto;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.cjg.traveling.domain.Alarm;

import jakarta.persistence.criteria.Predicate;

public class AlarmSpecs {
	
	public static Specification<Alarm> searchWithUserId(String userId){
		return (Specification<Alarm>)((root, query, builder) -> {
			Predicate predicateUserId = builder.equal(root.get("toUser").get("userId"), userId);
			return predicateUserId;
		});
	}

}
