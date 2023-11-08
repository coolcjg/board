package com.cjg.traveling.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class Encrypt {
	
	public String getSalt() {
		
		SecureRandom sr = new SecureRandom();
		byte[] salt = new byte[20];
		
		sr.nextBytes(salt);
		
		StringBuffer sb = new StringBuffer();
		for(byte b : salt) {
			sb.append(String.format("%02x", b));
		}
		
		return sb.toString();		
	}
	
	public String getEncrypt(String pwd, String salt) {
		String result = "";
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			
			md.update((pwd + salt).getBytes());
			byte[] pwdSalt = md.digest();
			
			StringBuffer sb = new StringBuffer();
			for(byte b : pwdSalt){
				sb.append(String.format("%02x", b));
			}
			
			result = sb.toString();
			
		}catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}

}
