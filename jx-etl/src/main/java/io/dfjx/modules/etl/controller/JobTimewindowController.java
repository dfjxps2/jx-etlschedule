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

import io.dfjx.modules.etl.entity.JobTimewindowEntity;
import io.dfjx.modules.etl.service.JobTimewindowService;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.R;



/**
 * 
 *
 * @author lwq
 * @email 404461275@qq.com
 * @date 2018-08-09 18:05:44
 */
@RestController
@RequestMapping("etl/jobtimewindow")
public class JobTimewindowController {
    @Autowired
    private JobTimewindowService jobTimewindowService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("etl:jobtimewindow:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = jobTimewindowService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("etl:jobtimewindow:info")
    public R info(@PathVariable("id") Integer id){
        JobTimewindowEntity jobTimewindow = jobTimewindowService.selectById(id);

        return R.ok().put("jobTimewindow", jobTimewindow);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("etl:jobtimewindow:save")
    public R save(@RequestBody JobTimewindowEntity jobTimewindow){
        jobTimewindowService.insert(jobTimewindow);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("etl:jobtimewindow:update")
    public R update(@RequestBody JobTimewindowEntity jobTimewindow){
        ValidatorUtils.validateEntity(jobTimewindow);
        jobTimewindowService.updateAllColumnById(jobTimewindow);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("etl:jobtimewindow:delete")
    public R delete(@RequestBody Integer[] ids){
        jobTimewindowService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
