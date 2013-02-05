package com.github.tommyjkk.lastlogin;

import java.math.BigDecimal;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TotalPlayTimeExecuter implements CommandExecutor{

	LastLogin lastlogin;
	public TotalPlayTimeExecuter(LastLogin plugin) {
		lastlogin=plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length==1 && sender.hasPermission("lastlogin.totalplaytime")){
			String name=args[0];
			if(lastlogin.isInTimeMap(name) && lastlogin.getServer().getPlayer(name)!=null){
				double dblTotalTime=lastlogin.getTotalTime(name);
				dblTotalTime=dblTotalTime/60;
				BigDecimal totalTime = new BigDecimal(dblTotalTime).setScale(2, BigDecimal.ROUND_FLOOR);
				sender.sendMessage("Total play time for " + ChatColor.BLUE + args[0] + " " + ChatColor.GOLD + totalTime + " hours");
			}
			else if(lastlogin.isInTimeMap(name) && lastlogin.getServer().getPlayer(name)==null){
				double dblTotalTime=lastlogin.getTotalTime(name);
				dblTotalTime=dblTotalTime/60;
				BigDecimal totalTime = new BigDecimal(dblTotalTime).setScale(2, BigDecimal.ROUND_FLOOR);
				sender.sendMessage("Total play time for " + ChatColor.BLUE + args[0] + " " + ChatColor.GOLD + totalTime + " hours");
			}
			
			else if(!lastlogin.isInTimeMap(name) && lastlogin.getServer().getPlayer(name)!=null){
				sender.sendMessage(ChatColor.BLUE + args[0] + ChatColor.WHITE + " needs to logout to get total play time.");
			}
			
			else {
				sender.sendMessage(ChatColor.BLUE + args[0] + ChatColor.WHITE + " has not been on before." );
			}
			return true;
		}
		return false;
	}

}
