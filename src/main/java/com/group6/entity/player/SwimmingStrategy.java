package com.group6.entity.player;

import com.group6.entity.common.Tile;

import java.util.List;

public interface SwimmingStrategy {
    boolean canDiveToTile(Tile destination);
    List<Tile> getAvailableDivingLocations();
}