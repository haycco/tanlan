package org.haycco.tanlan.server.auth.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.haycco.tanlan.user.api.vo.UserVo;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

	private UserVo user;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "refresh token")
    private String refreshToken;

    @ApiModelProperty(value = "融云token")
    private String rcToken;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
