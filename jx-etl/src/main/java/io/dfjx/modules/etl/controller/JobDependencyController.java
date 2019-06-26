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

import io.dfjx.modules.etl.entity.JobDependencyEntity;
import io.dfjx.modules.etl.service.JobDependencyService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;



/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-27 10:52:12
 */
@RestController
@RequestMapping("etl/jobdependency")
public class JobDependencyController {
    @Autowired
    private JobDependencyService jobDependencyService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:jobdependency:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = jobDependencyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{etlSystem}")
    @RequiresPermissions("etl:jobdependency:info")
    public R info(@PathVariable("etlSystem") String etlSystem){
        JobDependencyEntity jobDependency = jobDependencyService.selectById(etlSystem);

        return R.ok().put("jobDependency", jobDependency);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("etl:jobdependency:save")
    public R save(@RequestBody JobDependencyEntity jobDependency){
        jobDependencyService.insert(jobDependency);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("etl:jobdependency:update")
    public R update(@RequestBody JobDependencyEntity jobDependency){
        ValidatorUtils.validateEntity(jobDependency);
        jobDependencyService.updateAllColumnById(jobDependency);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("etl:jobdependency:delete")
    public R delete(@RequestBody String[] etlSystems){
        jobDependencyService.deleteBatchIds(Arrays.asList(etlSystems));

        return R.ok();
    }

}
