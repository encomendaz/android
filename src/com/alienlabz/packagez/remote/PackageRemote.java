package com.alienlabz.packagez.remote;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alienlabz.packagez.model.Pack;

public class PackageRemote extends BaseRemote {
	public static final String SERVICE_URL = BASE_URL + "track/";

	public static List<Pack> findAll() {
		List<Pack> result = new ArrayList<Pack>();
		String strResult = performGet(SERVICE_URL + "list");

		try {
			JSONObject jsonObject = new JSONObject(strResult);
			JSONArray array = jsonObject.getJSONArray("result");
			for (int i = 0; i < array.length(); i++) {
				JSONObject packJson = array.getJSONObject(i);
				Pack pack = new Pack();
				pack.fromJSON(packJson);
				result.add(pack);
			}
		} catch (JSONException e) {
		}

		return result;
	}

	public static void delete(Pack pack) {
		// TODO Auto-generated method stub
		
	}

	public static void insert(Pack pack) {
		// TODO Auto-generated method stub
		
	}

}
