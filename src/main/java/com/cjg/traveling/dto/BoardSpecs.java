package com.cjg.traveling.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.cjg.traveling.domain.Board;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class BoardSpecs {
	
	public static Specification<Board> searchWith(Map<String, Object> searchKeyword){
		return (Specification<Board>)((root, query, builder) -> {
			List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);
			return builder.and(predicate.toArray(new Predicate[0]));
		});
	}
	
	private static List<Predicate> getPredicateWithKeyword(Map<String, Object> searchKeyword, Root<Board> root, CriteriaBuilder builder) {
			List<Predicate> predicate = new ArrayList<>();
			
			if(searchKeyword.get("searchType").equals("all")) {
				Predicate predicateForTitle = builder.like(root.get("title"), "%"+ searchKeyword.get("searchText") + "%");
				Predicate predicateForRegion = builder.like(root.get("region"), "%"+ searchKeyword.get("searchText") + "%");
				Predicate predicateForUserId = builder.like(root.get("user").get("userId"), "%"+ searchKeyword.get("searchText") + "%");
				Predicate predicateResult = builder.or(predicateForTitle, predicateForRegion, predicateForUserId);
				predicate.add(predicateResult);
			}else {
				if(searchKeyword.get("searchType").toString().equals("userId")) {
					predicate.add(builder.like(root.get("user").get("userId"), "%"+ searchKeyword.get("searchText") + "%"));
				}else if(searchKeyword.get("searchType").toString().equals("regDate")){
					
					String startDate = searchKeyword.get("searchText").toString().split("~")[0];
					String endDate = searchKeyword.get("searchText").toString().split("~")[1];
					
					LocalDateTime start = LocalDateTime.of(Integer.parseInt(startDate.split("-")[0]), Integer.parseInt(startDate.split("-")[1]), Integer.parseInt(startDate.split("-")[2]), 0, 0);
					LocalDateTime end = LocalDateTime.of(Integer.parseInt(endDate.split("-")[0]), Integer.parseInt(endDate.split("-")[1]), Integer.parseInt(endDate.split("-")[2]), 0, 0);
					end = end.plusDays(1);
					
					Predicate predicateForRegdate = builder.between(root.get("regDate"), start, end);
					predicate.add(predicateForRegdate);
					
				}else {
					predicate.add(builder.like(root.get(searchKeyword.get("searchType").toString()), "%"+ searchKeyword.get("searchText") + "%"));
				}
			}
			
			return predicate;
	}
}
