package io.dfjx.modules.etl.controller;

import java.util.Arrays;
import java.util.Map;

import io.dfjx.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.dfjx.modules.etl.entity.JobStreamEntity;
import io.dfjx.modules.etl.service.JobStreamService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;



/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-08-07 10:42:51
 */
@RestController
@RequestMapping("etl/jobstream")
public class JobStreamController {
    @Autowired
    private JobStreamService jobStreamService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:jobstream:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = jobStreamService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{etlSystem}")
    @RequiresPermissions("etl:jobstream:info")
    public R info(@PathVariable("etlSystem") String etlSystem){
        JobStreamEntity jobStream = jobStreamService.selectById(etlSystem);

        return R.ok().put("jobStream", jobStream);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("etl:jobstream:save")
    public R save(@RequestBody JobStreamEntity jobStream){
        jobStreamService.insert(jobStream);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("etl:jobstream:update")
    public R update(@RequestBody JobStreamEntity jobStream){
        ValidatorUtils.validateEntity(jobStream);
        jobStreamService.updateAllColumnById(jobStream);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("etl:jobstream:delete")
    public R delete(@RequestBody String[] etlSystems){
        jobStreamService.deleteBatchIds(Arrays.asList(etlSystems));

        return R.ok();
    }

}
