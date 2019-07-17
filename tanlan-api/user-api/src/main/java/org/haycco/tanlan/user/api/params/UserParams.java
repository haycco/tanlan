package org.haycco.tanlan.user.api.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.URL;

/**
 * @author haycco
 **/
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class UserParams {

    @ApiModelProperty(value = "用户头像", position = 1, example = "http://XX.XX.jpg")
    @URL(message = "头像路径不正确")
    private String thumbnail;

    @ApiModelProperty(value = "昵称", example = "hello", position = 2)
    private String nickname;

    @ApiModelProperty(value = "pickId", required = true, example = "XXXXXXXXXXXX")
    private String pickId;

    @ApiModelProperty(value = "性别：0 - 未知；1 - 男；2 - 女", position = 3, example = "1")
    private Integer gender;

    @ApiModelProperty(value = "省份", example = "湖北省", position = 4)
    private String province;

    @ApiModelProperty(value = "城市", example = "武汉市", position = 5)
    private String city;

    @ApiModelProperty(value = "描述", example = "描述", position = 6)
    private String desc;

    @ApiModelProperty(value = "手机平台：1-IOS , 2-ANDROID", example = "1", position = 7)
    private Integer phoneSystem;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
