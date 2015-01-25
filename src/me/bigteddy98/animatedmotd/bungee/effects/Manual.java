package me.bigteddy98.animatedmotd.bungee.effects;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;

public class Manual {

	ArrayList<String> text = new ArrayList<>();
	int looper = 0;
	
	public String next(){
		
		if(looper > text.size() - 1){
			looper = 0;
		}
		
		String out = text.get(looper);
		looper++;
		
		return out;
	}
	
	public Manual(){
		
		text.add("\t-\tGTA\t-\t");
		text.add("-\t\tGTA\t\t-");
	}
	
	
}
