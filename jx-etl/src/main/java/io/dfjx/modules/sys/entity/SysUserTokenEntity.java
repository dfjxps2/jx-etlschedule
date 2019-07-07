/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjx.modules.sys.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户Token
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_user_token")
public class SysUserTokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户ID
	@TableId(type = IdType.INPUT)
	private String userId;
	//token
	private String token;
	//过期时间
	private Date expireTime;
	//更新时间
	private Date updateTime;

}