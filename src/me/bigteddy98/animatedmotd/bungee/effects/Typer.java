package me.bigteddy98.animatedmotd.bungee.effects;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;

public class Typer {

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
	
	public Typer(String message){
		
		int size = message.length();
		
		text.add("");
		
		for (int i = 0; i < size; i++) {
			System.out.println(i);
			
			if(message.charAt(i) == ChatColor.COLOR_CHAR){
				i = i+2;
			}else{
				String add = (message.substring(0,i+1));
				text.add(add);
				//System.out.println(add);
			}
		}
		
		//System.out.println(text);
	}
	
}


