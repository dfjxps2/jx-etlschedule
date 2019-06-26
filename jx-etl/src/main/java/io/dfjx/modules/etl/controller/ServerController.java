package io.dfjx.modules.etl.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;
import io.dfjx.common.validator.ValidatorUtils;
import io.dfjx.modules.etl.entity.ServerEntity;
import io.dfjx.modules.etl.service.ServerService;



/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-23 21:54:15
 */
@RestController
@RequestMapping("etl/server")
public class ServerController {
    @Autowired
    private ServerService serverService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:server:list")
    public R list(@RequestParam Map<String, Object> params){
        System.out.println("================"+params.toString());
        PageUtils page = serverService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{etlServer}")
    @RequiresPermissions("etl:server:info")
    public R info(@PathVariable("etlServer") String etlServer){
        ServerEntity server = serverService.selectById(etlServer);
        return R.ok().put("server", server);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("etl:server:save")
    public R save(@RequestBody ServerEntity server){
        serverService.insert(server);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("etl:server:update")
    public R update(@RequestBody ServerEntity server){
        ValidatorUtils.validateEntity(server);
        serverService.updateAllColumnById(server);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("etl:server:delete")
    public R delete(@RequestBody String[] etlServers){
        System.out.println("==============" + etlServers.toString());
        serverService.deleteBatchIds(Arrays.asList(etlServers));

        return R.ok();
    }
    
    @RequestMapping("/getService") 
    @RequiresPermissions("etl:server:getService")
    public R getService(){
    	List<Map<String, Object>> listMap  = serverService.getServices();
        return R.ok().put("allServers",listMap);
    }

}
