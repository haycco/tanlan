package org.haycco.tanlan.user.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author haycco
 **/
@Data
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    @ApiModelProperty(value = "用户ID")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "头像")
    private Integer accountType;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "个人简介")
    private String profile;

    @ApiModelProperty(value = "年龄", example = "18")
    private Integer age;

    @ApiModelProperty(value = "性别：0 - 未知；1 - 男；2 - 女；3 - 其他", example = "0")
    private Integer gender;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
