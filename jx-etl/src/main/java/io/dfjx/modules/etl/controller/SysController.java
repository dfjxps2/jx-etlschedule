package io.dfjx.modules.etl.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
import io.dfjx.modules.etl.entity.SysEntity;
import io.dfjx.modules.etl.service.SysService;



/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-23 22:44:03
 */
@RestController
@RequestMapping("etl/sys")
public class SysController {
    @Autowired
    private SysService sysService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:sys:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{etlSystem}")
    @RequiresPermissions("etl:sys:info")
    public R info(@PathVariable("etlSystem") String etlSystem){
        SysEntity sys = sysService.selectById(etlSystem);

        return R.ok().put("sys", sys);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("etl:sys:save")
    public R save(@RequestBody SysEntity sys){
        sysService.insert(sys);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("etl:sys:update")
    public R update(@RequestBody SysEntity sys){
        ValidatorUtils.validateEntity(sys);
        sysService.updateAllColumnById(sys);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("etl:sys:delete")
    public R delete(@RequestBody String[] etlSystems){
        sysService.deleteBatchIds(Arrays.asList(etlSystems));

        return R.ok();
    }

    @RequestMapping("/getsys")
    public R getEtlSys(){
        List<String> etlsys = sysService.getEtlSys();
        List<LinkedHashMap<String, String>> listMap = new  ArrayList<LinkedHashMap<String, String>>();
        LinkedHashMap<String, String>  map=new LinkedHashMap<String, String>();
        for(String sys:etlsys){
        	map=new LinkedHashMap<String, String>();
        	map.put("code",sys);
        	map.put("name",sys);
        	listMap.add(map);
        }
        return R.ok().put("allsys",listMap);
    }

}
