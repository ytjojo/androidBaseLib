package com.kerkr.edu.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.kerkr.edu.app.Constans;
import com.kerkr.edu.log.VALog;

public class MultipartRequest extends Request<String> {

	private MultipartRequestParams params = null;
	private HttpEntity httpEntity = null;
	private final Listener<String> mListener;

	public MultipartRequest(String url, MultipartRequestParams params,Listener<String> l) {
		this(Method.POST, params, url,l);
	}

	public MultipartRequest(int method, MultipartRequestParams params,
			String url,Listener<String> l) {
		super(method, HttpConfig.getUrl()+ url, null);
		// TODO Auto-generated constructor stub
		this.params = params;
		this.mListener=l;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (params != null) {
			httpEntity = params.getEntity();
			try {
				httpEntity.writeTo(baos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		try {
			String jsonString =new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(response));
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		Map<String, String> headers = super.getHeaders();
		if (null == headers || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}

		// TODO setHeaders;
		return headers;
	}

	@Override
	public String getBodyContentType() {
		// TODO Auto-generated method stub
		String str = httpEntity.getContentType().getValue();
		return httpEntity.getContentType().getValue();
	}

	@Override
	protected void deliverResponse(String response) {
		// TODO Auto-generated method stub
		mListener.onResponse(response);
	}

	@Override
	public void deliverError(VolleyError error) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onResponse(error.toString());
		}
	}
}
