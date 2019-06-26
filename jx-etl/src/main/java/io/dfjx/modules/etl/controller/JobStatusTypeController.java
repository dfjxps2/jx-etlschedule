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

import io.dfjx.modules.etl.entity.JobStatusTypeEntity;
import io.dfjx.modules.etl.service.JobStatusTypeService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;



/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-07-29 12:57:36
 */
@RestController
@RequestMapping("etl/jobstatustype")
public class JobStatusTypeController {
    @Autowired
    private JobStatusTypeService jobStatusTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:jobstatustype:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = jobStatusTypeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("etl:jobstatustype:info")
    public R info(@PathVariable("id") Integer id){
        JobStatusTypeEntity jobStatusType = jobStatusTypeService.selectById(id);

        return R.ok().put("jobStatusType", jobStatusType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("etl:jobstatustype:save")
    public R save(@RequestBody JobStatusTypeEntity jobStatusType){
        jobStatusTypeService.insert(jobStatusType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("etl:jobstatustype:update")
    public R update(@RequestBody JobStatusTypeEntity jobStatusType){
        ValidatorUtils.validateEntity(jobStatusType);
        jobStatusTypeService.updateAllColumnById(jobStatusType);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("etl:jobstatustype:delete")
    public R delete(@RequestBody Integer[] ids){
        jobStatusTypeService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
