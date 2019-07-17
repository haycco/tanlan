package org.haycco.tanlan.user.api.params;

import io.swagger.annotations.ApiModel;
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
public class PhoneLoginParams {

    private String phone;
    private String password;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
