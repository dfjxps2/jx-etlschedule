package io.dfjx.modules.etl.service.impl;

import io.dfjx.modules.etl.vo.EtlJobStatusTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;

import io.dfjx.modules.etl.dao.JobStatusTypeDao;
import io.dfjx.modules.etl.entity.JobStatusTypeEntity;
import io.dfjx.modules.etl.service.JobStatusTypeService;


@Service("jobStatusTypeService")
public class JobStatusTypeServiceImpl extends ServiceImpl<JobStatusTypeDao, JobStatusTypeEntity> implements JobStatusTypeService {

    @Autowired
    private JobStatusTypeDao jobStatusTypeDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JobStatusTypeEntity> page = this.selectPage(
                new Query<JobStatusTypeEntity>(params).getPage(),
                new EntityWrapper<JobStatusTypeEntity>()
        );

        return new PageUtils(page);
    }

    public List<EtlJobStatusTypeVO> getStatusMap(){
//        return jobStatusTypeDao.selectMaps(
//                new EntityWrapper<JobStatusTypeEntity>()
//                .setSqlSelect("status","status_desc")
//
//        );
        List<EtlJobStatusTypeVO> volist = new ArrayList<>();
        List<JobStatusTypeEntity> jobStatusTypeEntityList =
                jobStatusTypeDao.selectList(new EntityWrapper<JobStatusTypeEntity>());
        for(JobStatusTypeEntity en : jobStatusTypeEntityList){
//            resultMap.put(en.getStatus(),en.getStatusDesc());
            EtlJobStatusTypeVO vo = new EtlJobStatusTypeVO();
            vo.setStatus(en.getStatus());
            vo.setStatus_desc(en.getStatusDesc());
            volist.add(vo);

        }

        return volist;
    };
}
