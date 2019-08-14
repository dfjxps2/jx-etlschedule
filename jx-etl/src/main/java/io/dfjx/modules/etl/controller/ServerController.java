package io.dfjx.modules.etl.controller;

import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;
import io.dfjx.common.validator.ValidatorUtils;
import io.dfjx.modules.etl.entity.ServerEntity;
import io.dfjx.modules.etl.service.ServerService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



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
    private static Logger logger = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private ServerService serverService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:server:list")
    public R list(@RequestParam Map<String, Object> params){
        logger.info("================"+params.toString());
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
        logger.info("==============" + etlServers.toString());
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
