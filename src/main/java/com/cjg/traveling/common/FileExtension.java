package com.cjg.traveling.common;

public class FileExtension {
	
	private static String[] video = {"webm", "mkv", "avi", "ts", "mov", "wmv", "asf", "mp4", "mpeg", "flv"};
	private static String[] audio = {"wav", "flac", "mp3", "aac", "flac", "ogg", "wma"};
	private static String[] image = {"jpeg", "jpg", "tiff", "gif", "bmp", "png"};
	
	private static String[] ban = {"js", "exe"};
	
	public static String checkExtension(String fileName) {
		
		String ext = fileName.substring(fileName.indexOf(".")+1);
		
		for(String element : ban) {
			if(element.equalsIgnoreCase(ext)) {
				return "ban";
			}
		}
		
		for(String element : video) {
			if(element.equalsIgnoreCase(ext)) {
				return "video";
			}
		}	
		
		for(String element : audio) {
			if(element.equalsIgnoreCase(ext)) {
				return "audio";
			}
		}
		
		for(String element : image) {
			if(element.equalsIgnoreCase(ext)) {
				return "image";
			}
		}
		
		return "document";
	}
}

