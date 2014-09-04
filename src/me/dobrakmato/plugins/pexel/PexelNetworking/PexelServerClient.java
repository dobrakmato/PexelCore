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
package me.dobrakmato.plugins.pexel.PexelNetworking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Class used for comunicating with pexel compactibile servers.
 * 
 * @author Mato Kormuth
 * 
 */
public class PexelServerClient
{
	private Socket			clientSocket;
	private PacketHandler	handler;
	private final boolean	serverInstance;
	
	public PexelServerClient(final String masterIp, final int masterPort)
	{
		try
		{
			this.clientSocket.connect(new InetSocketAddress(
					InetAddress.getByName(masterIp), masterPort));
			this.login();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.serverInstance = false;
	}
	
	private void login()
	{
		//TODO: Login to master
		
		//Read loop.
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				Thread.currentThread().setName("PexelServerClient-ReadThread");
				PexelServerClient.this.readCycle();
			}
		}).start();
	}
	
	protected void readCycle()
	{
		try
		{
			this.handler = new PacketHandler(new DataInputStream(
					this.clientSocket.getInputStream()), false);
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		while (!this.clientSocket.isClosed())
		{
			try
			{
				this.handler.handlePacket();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public PexelServerClient(final Socket socket)
	{
		this.clientSocket = socket;
		try
		{
			this.handler = new PacketHandler(new DataInputStream(
					socket.getInputStream()), true);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.serverInstance = true;
	}
	
	public boolean isServerInstance()
	{
		return this.serverInstance;
	}
	
	public InetAddress getAddress()
	{
		return this.clientSocket.getInetAddress();
	}
	
	public void sendPacket(final PexelPacket packet)
	{
		try
		{
			this.handler.sendPacket(packet, this);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isConnected()
	{
		return !this.clientSocket.isClosed();
	}
	
	public PacketHandler getHandler()
	{
		return this.handler;
	}
	
	public DataOutputStream getOutputStream()
	{
		try
		{
			return new DataOutputStream(this.clientSocket.getOutputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
