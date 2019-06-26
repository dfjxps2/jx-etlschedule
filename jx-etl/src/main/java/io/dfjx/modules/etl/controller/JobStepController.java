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

import io.dfjx.modules.etl.entity.JobStepEntity;
import io.dfjx.modules.etl.service.JobStepService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;



/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-08-07 15:49:14
 */
@RestController
@RequestMapping("etl/jobstep")
public class JobStepController {
    @Autowired
    private JobStepService jobStepService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:jobstep:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = jobStepService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("etl:jobstep:info")
    public R info(@PathVariable("id") Integer id){
        JobStepEntity jobStep = jobStepService.selectById(id);

        return R.ok().put("jobStep", jobStep);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("etl:jobstep:save")
    public R save(@RequestBody JobStepEntity jobStep){
        jobStepService.insert(jobStep);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("etl:jobstep:update")
    public R update(@RequestBody JobStepEntity jobStep){
        ValidatorUtils.validateEntity(jobStep);
        jobStepService.updateAllColumnById(jobStep);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("etl:jobstep:delete")
    public R delete(@RequestBody Integer[] ids){
        jobStepService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
