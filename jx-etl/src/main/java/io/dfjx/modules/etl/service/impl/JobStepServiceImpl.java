package io.dfjx.modules.etl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;

import io.dfjx.modules.etl.dao.JobStepDao;
import io.dfjx.modules.etl.entity.JobStepEntity;
import io.dfjx.modules.etl.service.JobStepService;


@Service("jobStepService")
public class JobStepServiceImpl extends ServiceImpl<JobStepDao, JobStepEntity> implements JobStepService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JobStepEntity> page = this.selectPage(
                new Query<JobStepEntity>(params).getPage(),
                new EntityWrapper<JobStepEntity>()
        );

        return new PageUtils(page);
    }

}
