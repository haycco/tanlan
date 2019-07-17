package org.haycco.tanlan.user.api.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author haycco
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePwdParams {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户新密码")
    private String password;
}
