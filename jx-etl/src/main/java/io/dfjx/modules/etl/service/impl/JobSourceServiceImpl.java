package io.dfjx.modules.etl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;

import io.dfjx.modules.etl.dao.JobSourceDao;
import io.dfjx.modules.etl.entity.JobSourceEntity;
import io.dfjx.modules.etl.service.JobSourceService;


@Service("jobSourceService")
public class JobSourceServiceImpl extends ServiceImpl<JobSourceDao, JobSourceEntity> implements JobSourceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JobSourceEntity> page = this.selectPage(
                new Query<JobSourceEntity>(params).getPage(),
                new EntityWrapper<JobSourceEntity>()
        );

        return new PageUtils(page);
    }

}
