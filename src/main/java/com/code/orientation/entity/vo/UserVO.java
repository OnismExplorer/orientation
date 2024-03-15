package com.code.orientation.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserVO {
    private Long uid;
    private String avatar;
    private String username;
    private String nickname;
}
