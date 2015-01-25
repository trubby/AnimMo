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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import me.bigteddy98.animatedmotd.bungee.ping.ServerData;
import me.bigteddy98.animatedmotd.bungee.ping.StatusListener;
import net.md_5.bungee.api.ChatColor;

public class AnimatedFavicon implements StatusListener {

	private List<BufferedImage> images = new ArrayList<BufferedImage>();
	{
		// TODO load these images here however you want
		// the images have to be PNG images with a 64x64 pixel size!

		// there also are some easy tools available which allow you to create a
		// list of BufferedImages out of a .gif file, but it's up to you how you
		// do this ;)
	}
	private int currentImage = 0;

	@Override
	public ServerData update() {
		int nextImage = this.currentImage++;
		if (nextImage >= images.size()) {
			// as soon as the current image exceeds the size of the images
			// array, start all over again
			nextImage = 0;
			this.currentImage = 0;
		}
		return new ServerData("MOTD1", "MOTD2", this.images.get(nextImage), 300, ChatColor.GRAY + "%COUNT%/%MAX%");
	}
}
