package com.libo.springboot.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
    public class VxVo {

    /** encryptedData */
    @ApiModelProperty("encryptedData")
    private String encryptedData;

    /** iv */
    @ApiModelProperty("iv")
    private String iv;

    /** session_key */
    @ApiModelProperty("session_key")
    private String session_key;
}

