package org.haycco.tanlan.user.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author haycco
 **/
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo extends UserVo {

    @ApiModelProperty(value = "是否第一次登陆")
    private Boolean firstLogin;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
