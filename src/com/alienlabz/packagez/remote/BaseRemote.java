package com.alienlabz.packagez.remote;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alienlabz.packagez.model.User;

/**
 * Base class for remote services.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
abstract public class BaseRemote {
	public static final String BASE_URL = "http://packagez-alienlabs.rhcloud.com/";
	public static final String APP_ID = "1234567890";

	/**
	 * Perform a GET operation to a URL.
	 * 
	 * @param url URL.
	 * @return
	 */
	public static final String performGet(final String url) {
		String result = null;

		try {
			final URI uri = new URI(url);
			final HttpGet httpGet = new HttpGet(uri);
			final HttpClient httpClient = new DefaultHttpClient();

			httpGet.addHeader("appId", APP_ID);
			if (User.getDefault() != null) {
				httpGet.addHeader("accessToken", User.getDefault().token);
			}

			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {

					HttpEntity entity = response.getEntity();
					return EntityUtils.toString(entity);
				}

			};

			result = httpClient.execute(httpGet, responseHandler);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
