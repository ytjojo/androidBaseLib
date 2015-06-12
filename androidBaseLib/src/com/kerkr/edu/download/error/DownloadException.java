/**
 * Copyright (c) www.bugull.com
 */
package com.kerkr.edu.download.error;


public class DownloadException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public DownloadException(String errorCode) {
		super(errorCode);
	}
}
