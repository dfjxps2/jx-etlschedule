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

import io.dfjx.modules.etl.entity.JobSourceEntity;
import io.dfjx.modules.etl.service.JobSourceService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;



/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-08-07 15:37:39
 */
@RestController
@RequestMapping("etl/jobsource")
public class JobSourceController {
    @Autowired
    private JobSourceService jobSourceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:jobsource:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = jobSourceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("etl:jobsource:info")
    public R info(@PathVariable("id") Integer id){
        JobSourceEntity jobSource = jobSourceService.selectById(id);

        return R.ok().put("jobSource", jobSource);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("etl:jobsource:save")
    public R save(@RequestBody JobSourceEntity jobSource){
        jobSourceService.insert(jobSource);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("etl:jobsource:update")
    public R update(@RequestBody JobSourceEntity jobSource){
        ValidatorUtils.validateEntity(jobSource);
        jobSourceService.updateAllColumnById(jobSource);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("etl:jobsource:delete")
    public R delete(@RequestBody Integer[] ids){
        jobSourceService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
