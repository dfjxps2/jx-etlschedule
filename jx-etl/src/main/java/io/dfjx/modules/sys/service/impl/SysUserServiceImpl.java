/**
 * Copyright 2018 东方金信
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.dfjx.modules.sys.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dfjinxin.commons.auth.compoment.OauthUserTemplate;
import com.seaboxdata.auth.api.dto.OauthUserDTO;
import io.dfjx.common.annotation.DataFilter;
import io.dfjx.common.utils.Constant;
import io.dfjx.common.utils.CookieUtils;
import io.dfjx.common.utils.PageUtils;
import io.dfjx.common.utils.Query;
import io.dfjx.modules.sys.dao.SysUserDao;
import io.dfjx.modules.sys.entity.SysDeptEntity;
import io.dfjx.modules.sys.entity.SysUserEntity;
import io.dfjx.modules.sys.service.SysDeptService;
import io.dfjx.modules.sys.service.SysUserRoleService;
import io.dfjx.modules.sys.service.SysUserService;
import io.dfjx.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;

	@Autowired
	private OauthUserTemplate oauthUserTemplate;

	@Resource
	private HttpServletRequest request;

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryUserById(Long userId) {
		String token = CookieUtils.get(request, Constant.ACCESS_TOKEN).getValue();
		if(StringUtils.isNotBlank(token)){
			token = token.toLowerCase().replace("bearer", "");
		}
		SysUserEntity sysUserEntity = new SysUserEntity();
		List<OauthUserDTO> userDTOS = oauthUserTemplate.queryUsersByIds(userId, token);
		if(!userDTOS.isEmpty()){
			sysUserEntity.setUserId(userDTOS.get(0).getId());
			sysUserEntity.setUsername(userDTOS.get(0).getUsername());
		}
		return sysUserEntity;
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		String username = (String)params.get("username");

		Page<SysUserEntity> page = this.selectPage(
			new Query<SysUserEntity>(params).getPage(),
			new EntityWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
		);

		for(SysUserEntity sysUserEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.selectById(sysUserEntity.getDeptId());
			sysUserEntity.setDeptName(sysDeptEntity.getName());
		}

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
		this.insert(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
		}
		this.updateById(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}


	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new EntityWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

}
