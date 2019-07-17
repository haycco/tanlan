package org.haycco.tanlan.user.api.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author haycco
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PwdCheckParams {

    @ApiModelProperty(value = "用户id")
    @NotBlank
    private String id;

    @ApiModelProperty(value = "用户密码")
    @NotBlank
    private String password;
}
