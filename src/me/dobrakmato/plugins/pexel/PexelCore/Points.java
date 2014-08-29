package me.dobrakmato.plugins.pexel.PexelCore;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
