package com.cjg.traveling.common;

public class test {
	
	public static void main(String[] args) {
		
		int totalPage = 11;
		int pageNumber = 11;
		
		System.out.println("pageNumber : " + pageNumber + ", totalPage : " + totalPage);
		
		int start;
		int end;
		
		int a = pageNumber / 10;
		int b = pageNumber % 10;
		
		if(a==0) {
			start = 1;
			end = 10;
		}else {
			if(b==0) {
				end = a * 10;
				start = end - 9; 
			}else {
				end = (a+1) * 10;
				start = end -9;
			}
		}
		
		if(end > totalPage) {
			end = totalPage;
		}
		
		System.out.println("startPage : " + start + ", endPage : " + end);
		

	}
}
