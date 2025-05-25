package com.group6.entity.player.role;

import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;

public class Navigator extends Player {

    @Override
    public void performSpecialAbility(Object... params) {
        if (params.length >= 3 && params[0] instanceof Player && params[1] instanceof Tile && params[2] instanceof Integer) {
            Player targetPlayer = (Player) params[0];
            Tile destination = (Tile) params[1];
            Integer moveCount = (Integer) params[2];

            // 限制移动次数不超过2次
            int effectiveMoves = Math.min(moveCount, 2);

            // 执行导航移动
            navigateMove(targetPlayer, destination, effectiveMoves);
        }
    }

    private void navigateMove(Player targetPlayer, Tile destination, int moveCount) {
        // 检查目标玩家是否可以移动到指定位置
        if (canNavigateTo(targetPlayer, destination)) {
            // 消耗移动次数
            setActions(getActions() - moveCount);

            // 执行移动
            targetPlayer.setCurrentPosition(destination);
        }
    }

    private boolean canNavigateTo(Player targetPlayer, Tile destination) {
        // 检查移动次数是否足够
        if (getActions() < 1) {
            return false;
        }

        // 基础移动规则：目标位置必须与导航员相邻
        if (getCurrentPosition().isNearBy(destination)) {
            return true;
        }

        // 特殊规则：如果目标玩家是探险家，允许斜向移动
        if (targetPlayer instanceof Explorer) {
            double dx = Math.abs(getCurrentPosition().getPosition().getX() - destination.getPosition().getX());
            double dy = Math.abs(getCurrentPosition().getPosition().getY() - destination.getPosition().getY());
            return dx <= 1 && dy <= 1; // 允许斜向移动
        }

        // 特殊规则：如果目标玩家是潜水员，允许穿越淹没区域
        if (targetPlayer instanceof Diver) {
            Diver diver = (Diver) targetPlayer;
            return diver.canDiveToTile(destination);
        }

        return false;
    }
}