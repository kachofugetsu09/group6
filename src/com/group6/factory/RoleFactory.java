package com.group6.factory;

import com.group6.entity.common.RoleType;
import com.group6.entity.player.Player;
import com.group6.entity.player.role.*;

public class RoleFactory {

    public static Player createRole(RoleType type) {
        switch (type) {
            case ENGINEER:
                return new Engineer();
            case PILOT:
                return new Pilot();
            case NAVIGATOR:
                return new Navigator();
            case DIVER:
                return new Diver();
            case EXPLORER:
                return new Explorer();
            case MESSENGER:
                return new Messenger();
            default:
                throw new IllegalArgumentException("未知的角色类型: " + type);
        }
    }
}
