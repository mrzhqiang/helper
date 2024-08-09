package com.github.mrzhqiang.helper.third.detect.diting.dto;

import lombok.Data;

@Data
public class TextInspectOpenApiRequest {

    /**
     * 接⼊时对指定项⽬分配的权限验证密钥(16位，仅包
     * 含数字和⼤写字⺟)
     */
    private String token;

    /**
     * 唯一Id
     */
    private String data_id;

    /**
     * 待检测⽂本 限定2000个字符以内，⽂本⻓度超过2000个字符时，只检测前2000个字符
     */
    private String context;

    /**
     * ⽂本的类型，默认为"chat"，表示聊天记录
     * ⽀持“chat”(聊天消息),“nick”(昵称),"post"(帖⼦),"notice"(公告),"signature"(签名) 五种⽂本形式。
     * 五种⽂本形式因其⽂本⻓度有别，分别采⽤了不同的判别策略。此外，管理系统的词库的类型也与此类型对应
     */
    private String context_type;

}
