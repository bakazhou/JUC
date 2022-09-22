package com.cn.tw.graduate.bakazhou.Practice3.server.session;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;

/**
 * 聊天组，即聊天室
 */
@Data
public class Group {
    // 聊天室名称
    private String name;
    // 聊天室成员
    private Set<String> members;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }
}
