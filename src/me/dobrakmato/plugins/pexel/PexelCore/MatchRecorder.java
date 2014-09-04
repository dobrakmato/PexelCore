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
package me.dobrakmato.plugins.pexel.PexelCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Recording device for matches.
 * 
 * @author Mato Kormuth
 * 
 */
public class MatchRecorder
{
	private final MinigameArena			arena;
	private int							taskId		= 0;
	private final long					interval	= 2;
	private final List<Frame>			frames		= new ArrayList<Frame>(600);
	private final Map<UUID, String>		playernames	= new HashMap<UUID, String>();
	private final Map<UUID, Integer>	playerids	= new HashMap<UUID, Integer>();
	
	public MatchRecorder(final MinigameArena arena)
	{
		this.arena = arena;
	}
	
	public void startCapturing()
	{
		this.arena.chatAll(ChatColor.RED + "[Record] " + ChatColor.GOLD
				+ "Warning, this match is recorded!");
		this.arena.chatAll(ChatColor.RED + "[Record] " + ChatColor.GOLD
				+ "Recording started!");
		
		for (Player p : this.arena.activePlayers)
		{
			this.playernames.put(p.getUniqueId(), p.getName());
			this.playerids.put(p.getUniqueId(), p.getEntityId());
		}
		
		this.taskId = Pexel.schedule(new Runnable() {
			@Override
			public void run()
			{
				MatchRecorder.this.captureFrame();
			}
		}, 0L, this.interval);
	}
	
	protected void captureFrame()
	{
		Frame frame = new Frame();
		for (Player p : this.arena.activePlayers)
		{
			frame.p_locations.put(p.getEntityId(), p.getLocation());
			frame.p_healths.put(p.getEntityId(), ((CraftPlayer) p).getHealth()); //getHealth fix
		}
		this.frames.add(frame);
	}
	
	public void stopCapturing()
	{
		this.arena.chatAll(ChatColor.RED + "[Record] " + ChatColor.GOLD
				+ "Recording stopped!");
		Pexel.cancelTask(this.taskId);
	}
	
	public boolean isEnabled()
	{
		return this.taskId != 0;
	}
	
	public void save()
	{
		Log.info("Saving started!");
		long starttime = System.nanoTime();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-SS");
		String name = Paths.matchRecord(sdf.format(new Date()) + "-"
				+ this.arena.getName().toLowerCase());
		
		OutputStreamWriter writer = null;
		try
		{
			writer = new OutputStreamWriter(
					new FileOutputStream(new File(name)));
			
			writer.write("# MATCH RECORD INFO START\n");
			writer.write("version=1\n");
			writer.write("interval=" + this.interval + "\n");
			writer.write("# MATCH RECORD INFO END\n");
			
			writer.write("# MINIGAME INFO START\n");
			writer.write("minigameName=" + this.arena.getMinigame().getName()
					+ "\n");
			writer.write("arenaName=" + this.arena.areaName + "\n");
			writer.write("date=" + System.currentTimeMillis() + "\n");
			writer.write("# MINIGAME INFO END\n");
			
			writer.write("# NAME TRANSLATE MAP START\n");
			for (Entry<UUID, String> entry : this.playernames.entrySet())
				writer.write(entry.getKey().toString() + "=" + entry.getValue()
						+ "\n");
			writer.write("# NAME TRANSLATE MAP END\n");
			
			writer.write("# ID TRANSLATE MAP START\n");
			for (Entry<UUID, Integer> entry : this.playerids.entrySet())
				writer.write(entry.getKey().toString() + "=" + entry.getValue()
						+ "\n");
			writer.write("# ID TRANSLATE MAP END\n");
			
			writer.write("# FRAMES SECTION START\n");
			
			List<Frame> frames2 = this.frames;
			int frameCount = frames2.size();
			for (int i = 0; i < frameCount; i++)
			{
				Frame f = frames2.get(i);
				writer.write("# FRAME " + i + " START\n");
				
				writer.write("# FRAME PLAYER LOCATIONS LIST START\n");
				for (Entry<Integer, Location> entry : f.p_locations.entrySet())
				{
					writer.write(entry.getKey() + "=" + entry.getValue().getX()
							+ "|" + entry.getValue().getY() + "|"
							+ entry.getValue().getZ() + "|"
							+ entry.getValue().getYaw() + "|"
							+ entry.getValue().getPitch() + "\n");
				}
				writer.write("# FRAME PLAYER LOCATIONS LIST END\n");
				
				writer.write("# FRAME PLAYER HEALTH LIST START\n");
				for (Entry<Integer, Double> entry : f.p_healths.entrySet())
				{
					writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
				}
				writer.write("# FRAME PLAYER HEALTH LIST END\n");
				
				writer.write("# FRAME " + i + " END\n");
			}
			writer.write("# FRAMES SECTION END\n");
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		Log.info("Saving ended! (took " + (System.nanoTime() - starttime)
				+ " ns / " + (System.nanoTime() - starttime) / 1000 / 1000
				+ "ms)");
	}
	
	public class Frame
	{
		Map<Integer, Location>	p_locations	= new HashMap<Integer, Location>();
		Map<Integer, Double>	p_healths	= new HashMap<Integer, Double>();
	}
	
	public void reset()
	{
		this.frames.clear();
		this.playerids.clear();
		this.playernames.clear();
		
		Pexel.cancelTask(this.taskId);
	}
}
