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
package me.dobrakmato.plugins.pexel.PexelCore.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import me.dobrakmato.plugins.pexel.PexelCore.Pexel;

import org.bukkit.entity.Player;

/**
 * Class used for updating points.
 * 
 * @author Mato Kormuth
 * 
 */
public class Points
{
	private static final String	apiKey	= "secretkey369";
	
	/**
	 * Uploads player's points to webpage.
	 * 
	 * @param player
	 */
	protected static void pushPoints(final Player player)
	{
		PlayerProfile profile = StorageEngine.getProfile(player.getUniqueId());
		final int points = profile.getPoints();
		Pexel.getAsyncWorker().addTask(new Runnable() {
			@Override
			public void run()
			{
				Points.sendRequest("pnts=" + points);
			}
		});
	}
	
	private static void sendRequest(final String params)
	{
		String action = "updatePoints";
		try
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(
					"http://mertex.eu/pexel/api.php").openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes("apikey=" + Points.apiKey + "&action=" + action + "&"
					+ params);
			wr.flush();
			wr.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
