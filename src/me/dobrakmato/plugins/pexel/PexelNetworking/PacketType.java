package me.dobrakmato.plugins.pexel.PexelNetworking;

import java.util.HashMap;
import java.util.Map;

import me.dobrakmato.plugins.pexel.PexelNetworking.packets.CrossServerTeleportPacket;

/**
 * @author Mato Kormuth
 * 
 */
public enum PacketType
{
	CrossServerTeleportPacket(30, CrossServerTeleportPacket.class);
	
	private short							id;
	private Class<? extends PexelPacket>	clazz;
	
	private PacketType(final int id, final Class<? extends PexelPacket> clazz)
	{
		this.id = (short) id;
		this.clazz = clazz;
	}
	
	public short getId()
	{
		return this.id;
	}
	
	private Class<? extends PexelPacket> getClazz()
	{
		return this.clazz;
	}
	
	public static PacketType fromShort(final short id)
	{
		return PacketType.mappingShort.get(id);
	}
	
	public static PacketType fromClass(final Class<? extends PexelPacket> clazz)
	{
		return PacketType.mappingClass.get(clazz);
	}
	
	private static final Map<Short, PacketType>							mappingShort	= new HashMap<Short, PacketType>();
	private static final Map<Class<? extends PexelPacket>, PacketType>	mappingClass	= new HashMap<Class<? extends PexelPacket>, PacketType>();
	
	static
	{
		for (PacketType type : PacketType.values())
			if (PacketType.mappingShort.containsKey(type.getId()))
				throw new RuntimeException(
						"Wrong mapping in PacketType! Can't store more than one type for one ID.");
			else
				
				PacketType.mappingShort.put(type.getId(), type);
		
		for (PacketType type : PacketType.values())
			if (PacketType.mappingClass.containsKey(type.getId()))
				throw new RuntimeException(
						"Wrong mapping in PacketType! Can't store more than one type for one CLASS.");
			else
				
				PacketType.mappingClass.put(type.getClazz(), type);
	}
}
