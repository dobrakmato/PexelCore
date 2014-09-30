package eu.matejkormuth.pexel.PexelCore.cinematics.packets;

import net.minecraft.server.v1_7_R3.MathHelper;
import net.minecraft.server.v1_7_R3.PacketDataSerializer;
import net.minecraft.server.v1_7_R3.PacketListener;
import net.minecraft.server.v1_7_R3.PacketPlayOutListener;
import net.minecraft.server.v1_7_R3.PacketPlayOutSpawnEntity;

/**
 * @author M
 * 
 */
public class V3PacketPlayOutSpawnEntity extends PacketPlayOutSpawnEntity {
    
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;
    private int k;
    
    public V3PacketPlayOutSpawnEntity() {
    }
    
    public V3PacketPlayOutSpawnEntity(final int entityId, final double locX,
            final double locY, final double locZ, final float pitch, final float yaw,
            final int entityType, final int objectData, final double motX,
            final double motY, final double motZ) {
        this.a = entityId;
        this.b = MathHelper.floor(locX * 32.0D);
        this.c = MathHelper.floor(locY * 32.0D);
        this.d = MathHelper.floor(locZ * 32.0D);
        this.h = MathHelper.d(pitch * 256.0F / 360.0F);
        this.i = MathHelper.d(yaw * 256.0F / 360.0F);
        this.j = entityType;
        this.k = objectData;
        if (this.k > 0) {
            double d0 = motX;
            double d1 = motY;
            double d2 = motZ;
            double d3 = 3.9D;
            
            if (d0 < -d3) {
                d0 = -d3;
            }
            
            if (d1 < -d3) {
                d1 = -d3;
            }
            
            if (d2 < -d3) {
                d2 = -d3;
            }
            
            if (d0 > d3) {
                d0 = d3;
            }
            
            if (d1 > d3) {
                d1 = d3;
            }
            
            if (d2 > d3) {
                d2 = d3;
            }
            
            this.e = (int) (d0 * 8000.0D);
            this.f = (int) (d1 * 8000.0D);
            this.g = (int) (d2 * 8000.0D);
        }
    }
    
    @Override
    public void a(final PacketDataSerializer packetdataserializer) {
        this.a = packetdataserializer.a();
        this.j = packetdataserializer.readByte();
        this.b = packetdataserializer.readInt();
        this.c = packetdataserializer.readInt();
        this.d = packetdataserializer.readInt();
        this.h = packetdataserializer.readByte();
        this.i = packetdataserializer.readByte();
        this.k = packetdataserializer.readInt();
        if (this.k > 0) {
            this.e = packetdataserializer.readShort();
            this.f = packetdataserializer.readShort();
            this.g = packetdataserializer.readShort();
        }
    }
    
    @Override
    public void b(final PacketDataSerializer packetdataserializer) {
        packetdataserializer.b(this.a);
        packetdataserializer.writeByte(this.j);
        packetdataserializer.writeInt(this.b);
        packetdataserializer.writeInt(this.c);
        packetdataserializer.writeInt(this.d);
        packetdataserializer.writeByte(this.h);
        packetdataserializer.writeByte(this.i);
        packetdataserializer.writeInt(this.k);
        if (this.k > 0) {
            packetdataserializer.writeShort(this.e);
            packetdataserializer.writeShort(this.f);
            packetdataserializer.writeShort(this.g);
        }
    }
    
    @Override
    public void a(final PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }
    
    @Override
    public String b() {
        return String.format(
                "id=%d, type=%d, x=%.2f, y=%.2f, z=%.2f",
                new Object[] { Integer.valueOf(this.a), Integer.valueOf(this.j),
                        Float.valueOf(this.b / 32.0F), Float.valueOf(this.c / 32.0F),
                        Float.valueOf(this.d / 32.0F) });
    }
    
    @Override
    public void a(final int i) {
        this.b = i;
    }
    
    @Override
    public void b(final int i) {
        this.c = i;
    }
    
    @Override
    public void c(final int i) {
        this.d = i;
    }
    
    @Override
    public void d(final int i) {
        this.e = i;
    }
    
    @Override
    public void e(final int i) {
        this.f = i;
    }
    
    @Override
    public void f(final int i) {
        this.g = i;
    }
    
    @Override
    public void handle(final PacketListener packetlistener) {
        this.a((PacketPlayOutListener) packetlistener);
    }
    
}
