package com.group6.entity.common;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum RoleType {
    ENGINEER("工程师", "可以修复一个沉没的瓷砖"),
    PILOT("飞行员", "每回合可以飞到任意瓷砖"),
    NAVIGATOR("领航员", "可以移动其他玩家"),
    DIVER("潜水员", "可以穿过沉没的瓷砖"),
    EXPLORER("探险家", "可以斜向移动"),
    MESSENGER("信使", "可以将宝藏卡给任何玩家");

    private final String name;
    private final String ability;

    RoleType(String name, String ability) {
        this.name = name;
        this.ability = ability;
    }

}
