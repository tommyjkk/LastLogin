package com.github.tommyjkk.lastlogin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LastLoginCommandExecuter implements CommandExecutor{

	LastLogin lastlogin;
	public LastLoginCommandExecuter(LastLogin lastlogin1) {
		this.lastlogin=lastlogin1;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (args.length==1 && sender.hasPermission("lastlogin.lastlogin")){
			String name=args[0];
			name=name.toLowerCase();
			
			if(lastlogin.isInJoinMap(name)){
				DateTime joinDate=lastlogin.getJoinDate(name);
				 DateTimeFormatter fmt = DateTimeFormat.forPattern("hh:mmaa MMMM dd, yyyy");
				String strJoinDate=fmt.print(joinDate);
				sender.sendMessage("Last login for " + ChatColor.BLUE + args[0] + " " + ChatColor.GOLD + strJoinDate);
				return true;
				
			}
			else {
				sender.sendMessage("Could not find " + args[0]);
				return true;
			}
			
		}
		else return false;
		
	}

}
