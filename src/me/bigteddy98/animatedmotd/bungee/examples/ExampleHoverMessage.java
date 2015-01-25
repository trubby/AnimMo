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
package me.bigteddy98.animatedmotd.bungee.examples;

import java.util.ArrayList;
import java.util.List;

import me.bigteddy98.animatedmotd.bungee.ping.ServerData;
import me.bigteddy98.animatedmotd.bungee.ping.StatusListener;
import net.md_5.bungee.api.ChatColor;

public class ExampleHoverMessage implements StatusListener {

	@Override
	public ServerData update() {
		List<String> list = new ArrayList<String>();
		list.add(ChatColor.GOLD + "" + ChatColor.BOLD + "LINE1");
		list.add(ChatColor.RED + "" + ChatColor.BOLD + "LINE2");
		list.add(ChatColor.WHITE + "" + ChatColor.BOLD + "LINE3");
		return new ServerData("MOTD1", "MOTD2", 300, ChatColor.GRAY + "%COUNT%/%MAX%", list);
	}
}
