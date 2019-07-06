/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjx.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.dfjx.common.utils.R;
import io.dfjx.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	R createToken(String userId);

	/**
	 * 退出，修改token值
	 * @param userId  用户ID
	 */
	void logout(String userId);

}
