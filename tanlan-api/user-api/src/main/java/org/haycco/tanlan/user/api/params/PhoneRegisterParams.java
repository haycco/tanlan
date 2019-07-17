package org.haycco.tanlan.user.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.haycco.tanlan.common.constant.CommonConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author haycco
 **/
@Data
@ApiModel
public class PhoneRegisterParams extends UserParams {

    @ApiModelProperty(value = "手机号", example = "13345678911", required = true, position = 7)
    @Pattern(regexp = CommonConstant.MOBILE_REGEX, message = "手机号不合法")
    private String phone;

    @ApiModelProperty(value = "密码", example = "XXX", required = true, position = 8)
    @NotBlank
    private String password;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
