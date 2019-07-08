package io.dfjx.modules.etl.controller;

import com.alibaba.fastjson.JSONObject;
import io.dfjx.common.utils.R;
import io.dfjx.common.utils.WebClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/menu")
public class TaijiMenuController {

    @GetMapping("top")
    public R top() {
        String url = "http://172.26.60.219:8081/app/api_v1/getMenuUrlsByCaId";
        WebClient client = new WebClient();
        JSONObject json = client.post3(url, new HashMap(){{
            put("models", "{\"caId\":\"7ac39c275b1e7f3b744eb4e5e4257c61\"}");
        }});

        return R.ok(new HashMap() {{
            put("data", json.getJSONArray("data"));
        }});
    }
}
