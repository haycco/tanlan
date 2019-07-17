package org.haycco.tanlan.user.api.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.haycco.tanlan.common.constant.CommonConstant;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author haycco
 **/
@Data
@ApiModel
public class ResetPasswordParams {

    /**
     * 手机号
     */
    @Pattern(regexp = CommonConstant.MOBILE_REGEX, message = "手机号码不正确")
    private String phone;

    /**
     * 验证码
     */
    @NotEmpty(message = "验证码不能为空")
    private String code;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
