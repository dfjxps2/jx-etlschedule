package io.dfjx.modules.etl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;

import io.dfjx.modules.etl.dao.JobTimewindowDao;
import io.dfjx.modules.etl.entity.JobTimewindowEntity;
import io.dfjx.modules.etl.service.JobTimewindowService;


@Service("jobTimewindowService")
public class JobTimewindowServiceImpl extends ServiceImpl<JobTimewindowDao, JobTimewindowEntity> implements JobTimewindowService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JobTimewindowEntity> page = this.selectPage(
                new Query<JobTimewindowEntity>(params).getPage(),
                new EntityWrapper<JobTimewindowEntity>()
        );

        return new PageUtils(page);
    }

}
