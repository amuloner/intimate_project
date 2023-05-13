package com.wu.intimate.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_chat")
public class Chat {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer chatId;
    private Integer fromId;
    private Integer toId;
    private String content;
    private Integer status;
    private Integer lastChat;
    private Integer islast;
    private String ctime;//时间戳
    private Integer isdel;

    public Chat(Integer id, Integer islast){
        this.id = id;
        this.islast = islast;
    }
}
