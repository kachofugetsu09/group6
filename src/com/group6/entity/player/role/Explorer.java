package com.group6.entity.player.role;

import com.group6.controller.GameController;
import com.group6.entity.common.Tile;
import com.group6.entity.player.Player;
import com.group6.entity.player.SwimmingStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class Explorer extends Player implements SwimmingStrategy {

    @Override
    public void performSpecialAbility(Object... params) {

    }
    @Override
    public boolean move(Tile destination) {
        // 如果可以正常移动，使用普通移动规则
        if ((Math.abs(getCurrentPosition().getPosition().getX()-destination.getPosition().getX()) <=1&&
                Math.abs(getCurrentPosition().getPosition().getY()-destination.getPosition().getY())<=1)&& !
                destination.isFlooded() &&
                getActions() > 0) {
            setActions(getActions() - 1);
            setCurrentPosition(destination);
            return true;
        }

        // 潜水员特殊移动：通过被淹没的瓷砖
        if (canDiveToTile(destination) && getActions() > 0) {
            setCurrentPosition(destination);
            setActions(getActions() - 1);
            return true;
        }

        return false;
    }


    /**
     * 检查潜水员是否可以潜水到目标瓷砖
     */
    @Override
    public boolean canDiveToTile(Tile destination) {
        if (destination.isFlooded()) {
            return false;
        }

        int currentX = getCurrentPosition().getPosition().x;
        int currentY = getCurrentPosition().getPosition().y;

        int destX = destination.getPosition().x;
        int destY = destination.getPosition().y;

        if (currentX != destX && currentY != destY) {
            return false;
        }

        GameController gameController = getGameController();

        // 检查从当前位置到目标位置的路径
        if (currentX == destX) {
            int step = (destY > currentY) ? 1 : -1;
            boolean foundUnfloodedTile = false;

            for (int y = currentY + step; y != destY + step; y += step) {
                Tile tile = gameController.findTileAt(currentX, y);
                if (tile == null) {
                    return false;
                }
                if (!tile.isFlooded()) {
                    if (y == destY) {
                        return true;
                    }
                    foundUnfloodedTile = true;
                } else if (foundUnfloodedTile) {
                    return false;
                }
            }
        } else {
            int step = (destX > currentX) ? 1 : -1;
            boolean foundUnfloodedTile = false;

            for (int x = currentX + step; x != destX + step; x += step) {
                Tile tile = gameController.findTileAt(x, currentY);
                if (tile == null) {
                    return false;
                }
                if (!tile.isFlooded()) {
                    if (x == destX) {
                        return true;
                    }
                    foundUnfloodedTile = true;
                } else if (foundUnfloodedTile) {
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * 获取潜水员可以潜水到的所有瓷砖
     */
    @Override
    public List<Tile> getAvailableDivingLocations() {
        List<Tile> availableLocations = new ArrayList<>();
        GameController gameController = getGameController();

        // 检查四个方向
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        int currentX = getCurrentPosition().getPosition().x;
        int currentY = getCurrentPosition().getPosition().y;

        for (int direction = 0; direction < 4; direction++) {
            int x = currentX;
            int y = currentY;
            boolean foundFlooded = false;

            // 沿着一个方向移动，直到遇到未淹没的瓷砖或边界
            while (true) {
                x += dx[direction];
                y += dy[direction];

                Tile tile = gameController.findTileAt(x, y);
                if (tile == null) {
                    break;
                }

                if (tile.isFlooded()) {
                    foundFlooded = true;
                } else {
                    if (foundFlooded) {
                        availableLocations.add(tile);
                    }
                    break;
                }
            }
        }

        return availableLocations;
    }
}
