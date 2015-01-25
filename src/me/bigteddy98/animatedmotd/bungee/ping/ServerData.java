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
package me.bigteddy98.animatedmotd.bungee.ping;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ListenerInfo;

public class ServerData {

	private final String motd;
	private String favicon;
	private final int sleepTime;
	private final String format;
	private final List<String> players;

	public ServerData(int sleepTime, String format) {
		this(getDefaultMOTD(), "", sleepTime, format);
	}

	public ServerData(String motd1, String motd2, int sleepTime, String format) {
		this(motd1, motd2, null, sleepTime, format);
	}

	public ServerData(String motd1, String motd2, BufferedImage pngIcon, int sleepTime, String format) {
		this(motd1, motd2, pngIcon, sleepTime, format, new ArrayList<String>());
	}

	public ServerData(String motd1, String motd2, int sleepTime, String format, List<String> players) {
		this(motd1, motd2, null, sleepTime, format, players);
	}

	public ServerData(String motd1, String motd2, BufferedImage pngIcon, int sleepTime, String format, List<String> players) {
		if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', motd1)).length() > 60) {
			throw new IllegalArgumentException("MOTD can not be longer than 60 characters per line.");
		}
		if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', motd2)).length() > 60) {
			throw new IllegalArgumentException("MOTD can not be longer than 60 characters per line.");
		}
		this.motd = motd1 + "\n" + motd2;

		if (pngIcon != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				if (pngIcon.getWidth() == 64 && pngIcon.getHeight() == 64) {
					ImageIO.write(pngIcon, "png", baos);
					baos.flush();
					this.favicon = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
				} else {
					throw new RuntimeException("Your server-icon.png needs to be 64*64!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.players = players;
		this.sleepTime = sleepTime;
		this.format = format;
	}

	public String getMotd() {
		return motd;
	}

	public String getFavicon() {
		return favicon;
	}

	public int getSleepMillis() {
		return sleepTime;
	}

	public String getFormat() {
		return format;
	}

	public List<String> getPlayers() {
		return players;
	}

	@SuppressWarnings("deprecation")
	private static String getDefaultMOTD() {
		for (ListenerInfo listener : ProxyServer.getInstance().getConfig().getListeners()) {
			return listener.getMotd();
		}
		return "";
	}
}
