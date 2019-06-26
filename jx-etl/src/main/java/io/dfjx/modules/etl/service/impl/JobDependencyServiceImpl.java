package io.dfjx.modules.etl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;

import io.dfjx.modules.etl.dao.JobDependencyDao;
import io.dfjx.modules.etl.entity.JobDependencyEntity;
import io.dfjx.modules.etl.service.JobDependencyService;


@Service("jobDependencyService")
public class JobDependencyServiceImpl extends ServiceImpl<JobDependencyDao, JobDependencyEntity> implements JobDependencyService {

    @Autowired
    private JobDependencyDao jobDependencyDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JobDependencyEntity> page = this.selectPage(
                new Query<JobDependencyEntity>(params).getPage(),
                new EntityWrapper<JobDependencyEntity>()
        );

        return new PageUtils(page);
    }

    public List<JobDependencyEntity> getDependencyJobs(String etlSystem, String etlJob){
        return jobDependencyDao.selectDependencyJobs(etlSystem,etlJob);
    }

    @Override
    public List<Map> getDependencyAllJobs() {
        return jobDependencyDao.selectDependencyAllJobs();
    }

}
