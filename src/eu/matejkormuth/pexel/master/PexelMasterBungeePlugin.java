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
package eu.matejkormuth.pexel.master;

import java.lang.reflect.Field;

import net.md_5.bungee.BungeeSecurityManager;
import net.md_5.bungee.api.plugin.Plugin;

public class PexelMasterBungeePlugin extends Plugin {
    private static PexelMasterBungeePlugin instance;
    
    public PexelMasterBungeePlugin() {
        PexelMasterBungeePlugin.instance = this;
    }
    
    class CustomSecurityManager extends SecurityManager {
        
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        
        this.getLogger().info("[BOOT] Determinating BungeeCord patching status...");
        boolean patched = false;
        for (Field f : BungeeSecurityManager.class.getDeclaredFields()) {
            if (f.getName() == "PATCHED") {
                try {
                    if (f.getInt(null) == 12) {
                        patched = true;
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if (patched) {
            this.getLogger().info(
                    "[BOOT] BungeeCord seems to be patched! Have a nice day!");
        }
        else {
            this.getLogger()
                    .info("[BOOT] Sorry, but it seems like your BungeeCord jar file is not patched.");
            this.getLogger()
                    .info("[BOOT] BungeeCord does have SecurityManager that denies all actions except Bungee API calls to prevent access to machine by third party code.");
            this.getLogger()
                    .info("[BOOT] This disallows using PexelMaster on Bungee. Please use our patcher, to patch BungeeSecurityManager, to allow PexelMaster work properly.");
            this.getLogger()
                    .info("[BOOT] PexelMaster may still work, but functionality can be limited or it may crash unexceptably!");
        }
        
        this.createMaster();
    }
    
    public void createMaster() {
        // Get instance for first time - create PexelMaster.
        PexelMaster.init(this.getDataFolder());
        PexelMaster.getInstance();
    }
    
    public static Plugin getInstance() {
        return PexelMasterBungeePlugin.instance;
    }
}
