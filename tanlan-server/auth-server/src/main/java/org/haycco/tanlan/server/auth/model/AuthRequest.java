package org.haycco.tanlan.server.auth.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  AuthRequest {

    @ApiModelProperty(value = "手机号", example = "12345679810", required = true)
	private String username;

    @ApiModelProperty(value = "密码", example = "XXXXX", required = true)
	private String password;

	/**
	 * 登录类型
	 */
    @ApiModelProperty(value = "登陆类型", example = "password", required = true)
    private String grant_type;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
