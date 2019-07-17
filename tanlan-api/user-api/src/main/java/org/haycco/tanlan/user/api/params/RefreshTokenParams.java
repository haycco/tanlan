package org.haycco.tanlan.user.api.params;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author haycco
 **/
@Data
public class RefreshTokenParams {

    @NotBlank(message = "刷新token不能为空")
    private String refreshToken;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
