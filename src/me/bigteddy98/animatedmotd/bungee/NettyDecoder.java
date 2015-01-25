/* 
 * AnimatedMOTD BungeePlugin
 * Copyright (C) 2014 Sander Gielisse
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.bigteddy98.animatedmotd.bungee;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.bigteddy98.animatedmotd.bungee.ping.PingManager;
import me.bigteddy98.animatedmotd.bungee.ping.ServerData;
import me.bigteddy98.animatedmotd.bungee.ping.StatusListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.PingPacket;
import net.md_5.bungee.protocol.packet.StatusRequest;
import net.md_5.bungee.protocol.packet.StatusResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NettyDecoder extends MessageToMessageDecoder<PacketWrapper> {

	private final Main plugin;
	private StatusListener statusListener;
	private long previousTime = System.currentTimeMillis();
	private ChannelHandlerContext ctx;
	private ScheduledTask task;
	private boolean isPing = false;
	private final long startTime;
	private long requestedProtocol = -1;
	private boolean respondPing = false;

	public NettyDecoder(Main plugin) {
		this.plugin = plugin;
		this.startTime = System.currentTimeMillis();
		try {
			this.statusListener = PingManager.getPingManager().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		task = ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable() {

			public void run() {
				if (!isPing) {
					task.cancel();
				}
				if ((System.currentTimeMillis() - previousTime) > 10000) {
					if (isPing) {
						ctx.close();
						System.out.println("Animated Ping handler disconnected.");
					}
					task.cancel();
				}
			}
		}, 15L, 15L, TimeUnit.SECONDS);
	}

	@Override
	protected void decode(final ChannelHandlerContext ctx, PacketWrapper packet, List<Object> out) throws Exception {
		this.previousTime = System.currentTimeMillis();
		this.ctx = ctx;

		if (packet == null || packet.packet == null) {
			out.add(packet);
			return;
		}
		if (packet.packet instanceof Handshake) {
			Handshake packett = (Handshake) packet.packet;
			this.requestedProtocol = packett.getProtocolVersion();
		}
		if (packet.packet instanceof PingPacket) {
			if (respondPing) {
				ctx.pipeline().writeAndFlush(new PingPacket(((PingPacket) packet.packet).getTime()));
				ctx.close();
				return;
			}
			if ((System.currentTimeMillis() - startTime) > (PingManager.getStopAfter() * 1000)) {
				// respond with a response packet
				final ServerData data = this.statusListener.update();

				JsonObject version = new JsonObject();
				version.addProperty("name", "1.8");
				version.addProperty("protocol", requestedProtocol);

				JsonArray playerArray = new JsonArray();
				for (String playerName : data.getPlayers()) {
					JsonObject playerObject = new JsonObject();
					playerObject.addProperty("name", playerName);
					playerObject.addProperty("id", UUID.randomUUID().toString());
					playerArray.add(playerObject);
				}

				JsonObject countData = new JsonObject();
				countData.addProperty("max", getMaxCount());
				countData.addProperty("online", ProxyServer.getInstance().getOnlineCount());
				countData.add("sample", playerArray);

				JsonObject jsonObject = new JsonObject();
				jsonObject.add("version", version);
				jsonObject.add("players", countData);
				jsonObject.addProperty("description", data.getMotd());

				if (data.getFavicon() != null) {
					jsonObject.addProperty("favicon", data.getFavicon());
				}
				ctx.pipeline().writeAndFlush(new StatusResponse(jsonObject.toString()));
				respondPing = true;
			} else {
				final ServerData data = this.statusListener.update();
				this.isPing = true;
				ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable() {

					public void run() {
						// respond with a response packet
						ctx.pipeline().writeAndFlush((new StatusResponse(buildResponseJSON(data))));
					}
				}, data.getSleepMillis(), TimeUnit.MILLISECONDS);
			}
		} else if (packet.packet instanceof StatusRequest) {
			final ServerData data = this.statusListener.update();
			ctx.pipeline().writeAndFlush(new StatusResponse(buildResponseJSON(data)));
		} else {
			out.add(packet);
		}
	}

	@SuppressWarnings("deprecation")
	private int getMaxCount() {
		for (ListenerInfo listener : ProxyServer.getInstance().getConfig().getListeners()) {
			return listener.getMaxPlayers();
		}
		return -1;
	}

	private String buildResponseJSON(ServerData d) {
		final ServerData data = d;

		// respond with a response packet
		JsonObject version = new JsonObject();
		version.addProperty("name", data.getFormat().replace("%COUNT%", ProxyServer.getInstance().getOnlineCount() + "").replace("%MAX%", getMaxCount() + ""));
		version.addProperty("protocol", 10000);

		JsonArray playerArray = new JsonArray();
		for (String playerName : data.getPlayers()) {
			JsonObject playerObject = new JsonObject();
			playerObject.addProperty("name", playerName);
			playerObject.addProperty("id", UUID.randomUUID().toString());
			playerArray.add(playerObject);
		}

		JsonObject countData = new JsonObject();
		countData.addProperty("max", 0);
		countData.addProperty("online", 0);
		countData.add("sample", playerArray);

		JsonObject jsonObject = new JsonObject();
		jsonObject.add("version", version);
		jsonObject.add("players", countData);
		jsonObject.addProperty("description", data.getMotd());

		if (data.getFavicon() != null) {
			jsonObject.addProperty("favicon", data.getFavicon());
		}		
		return jsonObject.toString();
	}
}
