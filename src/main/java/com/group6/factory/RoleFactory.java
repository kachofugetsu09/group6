package com.group6.factory;

import com.group6.entity.common.RoleType;
import com.group6.entity.player.Player;
import com.group6.entity.player.role.*;

public class RoleFactory {

    public static Player createRole(RoleType type) {
        Player player;
        switch (type) {
            case ENGINEER:
                player =new Engineer();
                break;
            case PILOT:
                player =new Pilot();
                break;
            case NAVIGATOR:
                player =new Navigator();
                break;
            case DIVER:
                player =new Diver();
                break;
            case EXPLORER:
                player =new Explorer();
                break;
            case MESSENGER:
                player = new Messenger();
                break;
            default:
                throw new IllegalArgumentException("未知的角色类型: " + type);
        }
        player.setRoletype(type);
        return player;


    }
}
