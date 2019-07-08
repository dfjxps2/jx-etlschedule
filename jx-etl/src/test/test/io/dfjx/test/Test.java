package test.io.dfjx.test;

import com.alibaba.fastjson.JSONObject;
import io.dfjx.common.utils.WebClient;

import java.util.HashMap;

public class Test {
	public static void main(String[] args) {
		String url = "http://172.26.60.219:8081/app/api_v1/getMenuUrlsByCaId";
		WebClient client = new WebClient();
		JSONObject json = client.post3(url, new HashMap(){{
			put("models", "{\"caId\":\"7ac39c275b1e7f3b744eb4e5e4257c61\"}");
		}});

		System.out.println(json.getJSONArray("data").toString());
	}
}