package org.haycco.tanlan.server.auth.model;

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
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "refresh token")
    private String refreshToken;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
