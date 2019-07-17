package org.haycco.tanlan.user.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.haycco.tanlan.common.model.AbstractDocument;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * @author haycco
 */
@Document(collection = "users")
@TypeAlias("user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractDocument {

    private static final long serialVersionUID = -1132442987489771546L;
    /** 用户名 */
    private String username;
    /** 昵称 */
    private String nickname;
    /** 头像 */
    private String avatar;
    /** 手机号 */
    private String phone;
    /** 个人简介 */
    private String profile;
    /** 密码 */
    private String password;
    /** 年龄 */
    private Integer age;
    /** 性别 {@link org.haycco.tanlan.user.api.enums.UserGenderEnum} */
    private Integer gender;
    /** 省份 */
    private String province;
    /** 城市 */
    private String city;
    /** 最后登录时间 */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastLoggedInTime;
    /** 用户类型 {@link org.haycco.tanlan.user.api.enums.UserTypeEnum} */
    private Integer type = 0;
    /** 状态 {@link org.haycco.tanlan.user.api.enums.UserStatusEnum} */
    private Integer status;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
