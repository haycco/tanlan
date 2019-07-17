package org.haycco.tanlan.user.api.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.haycco.tanlan.common.constant.CommonConstant;

import javax.validation.constraints.Pattern;

/**
 * @author haycco
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPhoneParams {

    @ApiModelProperty(value = "用户id", example = "XXXXXXXXX")
    private String id;

    @ApiModelProperty(value = "手机号",example = "12345678910")
    @Pattern(regexp = CommonConstant.MOBILE_REGEX, message = "手机号不合法")
    private String phone;

    @ApiModelProperty(value = "验证码",example = "123456")
    private String code;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
