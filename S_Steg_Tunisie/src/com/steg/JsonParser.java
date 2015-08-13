package com.steg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

public class JsonParser {
	String strUrl, result;
	String a1, a2, a3, a4, a5, a6, a7, a8, a9, a10;
	InputStream is;


	/****************************************************/
	public JsonParser(String StrUrl, String A1, String A2,
			String A3, String A4, String A5, String A6, String A7, String A8,
			String A9, String A10) {
		is = null;
		strUrl = StrUrl;
		result = "";
		a1 = A1;
		a2 = A2;
		a3 = A3;
		a4 = A4;
		a5 = A5;
		a6 = A6;
		a7 = A7;
		a8 = A8;
		a9 = A9;
		a10 = A10;
	}

	/****************************************************************/
	public ArrayList<NameValuePair> initialisation(String A1, String A2,
			String A3, String A4, String A5, String A6, String A7, String A8,
			String A9, String A10) {
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("a1", A1));
		nameValuePairs.add(new BasicNameValuePair("a2", A2));
		nameValuePairs.add(new BasicNameValuePair("a3", A3));
		nameValuePairs.add(new BasicNameValuePair("a4", A4));
		nameValuePairs.add(new BasicNameValuePair("a5", A5));
		nameValuePairs.add(new BasicNameValuePair("a6", A6));
		nameValuePairs.add(new BasicNameValuePair("a7", A7));
		nameValuePairs.add(new BasicNameValuePair("a8", A8));
		nameValuePairs.add(new BasicNameValuePair("a9", A9));
		nameValuePairs.add(new BasicNameValuePair("a10", A10));
		return nameValuePairs;

	}

	/*************************************************************************/
	public InputStream Connect(String strURL) throws ClientProtocolException,
			IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(strURL);
		httppost.setEntity(new UrlEncodedFormEntity(initialisation(a1, a2, a3,
				a4, a5, a6, a7, a8, a9, a10)));
		HttpResponse response = httpclient.execute(httppost);
		if (response == null) {
			throw new ClientProtocolException("Error Network");
		}
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new IOException(" Error IOException");
		}
		InputStream s = entity.getContent();
		return s;
	}

	/*************************************************/
	public String ConvertToString(InputStream s) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(s,
					"iso-8859-1"), 8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		result = sb.toString();
		return result;
	}

	/**
	 * @throws JSONException ************************************************/
	public JSONArray Analyse(String rs)  {

		JSONArray jArray = null;
		try {
			jArray = new JSONArray(rs);
		} catch (JSONException e) {
			return null;
		}

		return jArray;
	}

}
