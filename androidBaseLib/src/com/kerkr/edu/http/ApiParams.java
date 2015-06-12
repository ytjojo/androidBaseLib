package com.kerkr.edu.http;

import java.util.HashMap;

public class ApiParams extends HashMap<String, String> {
	private static final long serialVersionUID = 8112047472727256876L;

	public ApiParams with(String key, String value) {
		put(key, value);
		return this;
	}
	public static ApiParams get(){
		return new ApiParams();
	}
}