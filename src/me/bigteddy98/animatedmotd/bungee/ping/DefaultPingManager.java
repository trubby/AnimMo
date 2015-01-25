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

import java.util.ArrayList;

import me.bigteddy98.animatedmotd.bungee.effects.Manual;
import me.bigteddy98.animatedmotd.bungee.effects.Typer;
import net.md_5.bungee.api.ChatColor;

public class DefaultPingManager implements StatusListener {

	//private final Scroller scroller1 = new Scroller(ChatColor.STRIKETHROUGH + "         " + ChatColor.WHITE + "[GTA]" + ChatColor.RESET + ChatColor.STRIKETHROUGH + "         ", 60, 10, '&');
	//private final Scroller scroller2 = new Scroller(ChatColor.GRAY + "[Close Beta]", 60, 10, '&');
	//private final Typer test = new Typer(ChatColor.GREEN + "testing :)" + ChatColor.RED + ":DDDDDDDDD");
	private final Manual test = new Manual();
	
	@Override
	public ServerData update() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("player 1");
		list.add("player 2");
		return new ServerData(test.next(), "test",100, ChatColor.GREEN + "%COUNT%" + ChatColor.WHITE + "/" + ChatColor.RED + "%MAX%", list);
	}
}
