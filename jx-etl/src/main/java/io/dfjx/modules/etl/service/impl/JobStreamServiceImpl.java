package io.dfjx.modules.etl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;

import io.dfjx.modules.etl.dao.JobStreamDao;
import io.dfjx.modules.etl.entity.JobStreamEntity;
import io.dfjx.modules.etl.service.JobStreamService;


@Service("jobStreamService")
public class JobStreamServiceImpl extends ServiceImpl<JobStreamDao, JobStreamEntity> implements JobStreamService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JobStreamEntity> page = this.selectPage(
                new Query<JobStreamEntity>(params).getPage(),
                new EntityWrapper<JobStreamEntity>()
        );

        return new PageUtils(page);
    }

}
