package io.dfjx.modules.etl.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;

import io.dfjx.modules.etl.dao.SysDao;
import io.dfjx.modules.etl.entity.SysEntity;
import io.dfjx.modules.etl.service.SysService;


@Service("sysService")
public class SysServiceImpl extends ServiceImpl<SysDao, SysEntity> implements SysService {

    @Autowired
    private SysDao sysDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        Page<SysEntity> page;
        if(params.containsKey("etlSystem")) {
            page = this.selectPage(
                    new Query<SysEntity>(params).getPage(),
                    new EntityWrapper<SysEntity>()
                            .like(StringUtils.isNotBlank(params.get("etlSystem").toString()), "etl_System", params.get("etlSystem").toString())
            );

        } else {
            page = this.selectPage(
                    new Query<SysEntity>(params).getPage(),
                    new EntityWrapper<SysEntity>());
        }
        return new PageUtils(page);
    }

    public List<String> getEtlSys(){
        return sysDao.selectEtlSys();
    }

}
