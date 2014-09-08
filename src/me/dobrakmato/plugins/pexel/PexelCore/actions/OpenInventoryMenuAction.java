// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform. 
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 * 
 * This file is part of Pexel.
 * 
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
package me.dobrakmato.plugins.pexel.PexelCore.actions;

import me.dobrakmato.plugins.pexel.PexelCore.menu.InventoryMenu;

import org.bukkit.entity.Player;

/**
 * Inventory menu action that opens another inventory menu.
 * 
 * @author Mato Kormuth
 * 
 */
public class OpenInventoryMenuAction implements Action {
    private final InventoryMenu im;
    
    /**
     * Creates new action that opens specified inventory menu when player clicks icon.
     * 
     * @param im
     *            another InventoryMenu
     */
    public OpenInventoryMenuAction(final InventoryMenu im) {
        this.im = im;
    }
    
    @Override
    public void load(final String string) {
        
    }
    
    @Override
    public String save() {
        return null;
    }
    
    @Override
    public void execute(final Player player) {
        this.im.showTo(player);
    }
}
