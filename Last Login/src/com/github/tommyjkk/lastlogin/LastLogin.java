package com.github.tommyjkk.lastlogin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class LastLogin extends JavaPlugin{
	HashMap<String, DateTime> joinDateMap=new HashMap<String, DateTime>();
	HashMap<String, DateTime> leaveDateMap=new HashMap<String, DateTime>();
	HashMap<String, Double> totalTimeMap=new HashMap<String, Double>();
	
	public boolean isInJoinMap(String name){
		return joinDateMap.containsKey(name);
	}
	public void putInJoin(String name, DateTime time){
		joinDateMap.put(name.toLowerCase(), time);
	}
	public DateTime getJoinDate(String name){
		return joinDateMap.get(name.toLowerCase());
	}
	public boolean isInLeaveMap(String name){
		return leaveDateMap.containsKey(name.toLowerCase());
	}
	public void putInLeave(String name, DateTime time){
		leaveDateMap.put(name.toLowerCase(), time);
	}
	public DateTime getLeaveDate(String name){
		return leaveDateMap.get(name.toLowerCase());
	}
	public void calcTotalTime(String name){
		name=name.toLowerCase();
		DateTime joinDate=this.getJoinDate(name.toLowerCase());
			DateTime now=DateTime.now();
			double minutes=Minutes.minutesBetween(joinDate, now).getMinutes();
			if (totalTimeMap.containsKey(name)){
				minutes=minutes+totalTimeMap.get(name);
			}
			totalTimeMap.put(name.toLowerCase(), minutes);
		
	}
	public Double getTotalTime(String name){
		return totalTimeMap.get(name.toLowerCase());
	}
	public boolean isInTimeMap(String name){
		return totalTimeMap.containsKey(name.toLowerCase());
	}
	
	
	public void checkFileExistsAndCreate(){
		if(!this.getDataFolder().exists()){
			this.getDataFolder().mkdir();
		}
	}
	public void checkTotalHashAndCreate(File PluginFile){
		File totalHash=new File(PluginFile, "Totals.dat");
		if(!totalHash.exists()){
			try{
				totalHash.createNewFile();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public void checkJoinHashAndCreate(File PluginFile){
		File joinHash=new File(PluginFile, "Joins.dat");
		if(!joinHash.exists()){
			try{
				joinHash.createNewFile();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void onEnable(){
		PluginManager manager=this.getServer().getPluginManager();
		Server server = this.getServer();
		manager.registerEvents(new LoginListener(this), this);
		this.getCommand("lastlogin").setExecutor(new LastLoginCommandExecuter(this));
		this.getCommand("totalplaytime").setExecutor(new TotalPlayTimeExecuter(this));
		server.getLogger().info("Last Login enabled.");
		
		
		
		this.checkFileExistsAndCreate();
		File PluginFile=this.getDataFolder();
		this.checkTotalHashAndCreate(PluginFile);
		this.checkJoinHashAndCreate(PluginFile);
		File joinHash=new File(PluginFile, "Joins.dat");
		File totalsHash= new File(PluginFile, "Totals.dat");
		boolean totalHashEmpty = totalsHash.length()==0;
		boolean joinHashEmpty=joinHash.length()==0;
		if (!joinHashEmpty){
			FileInputStream joinFileIS = null;
			try {
			
				joinFileIS=new FileInputStream(joinHash);
		
			} catch (FileNotFoundException e) {
	
				e.printStackTrace();

			}
	
			ObjectInputStream joinFileOIS = null;
	
			try {
		
				joinFileOIS= new ObjectInputStream(joinFileIS);
	
			} catch (IOException e) {
			
				e.printStackTrace();
		
			}
			try {
				joinDateMap=(HashMap<String, DateTime>) joinFileOIS.readObject();			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				joinFileOIS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(!totalHashEmpty){
			FileInputStream totalsFileIS=null;
			try {
				totalsFileIS=new FileInputStream(totalsHash);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			ObjectInputStream totalsFileOIS=null;
			try {
				totalsFileOIS=new ObjectInputStream(totalsFileIS);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				totalTimeMap=(HashMap<String, Double>) totalsFileOIS.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				totalsFileOIS.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void onDisable(){
		Player[] playersOnline=this.getServer().getOnlinePlayers();
		
		int amountOnline=playersOnline.length;
		int counter=0;
		String playerName=null;
		while(counter<=amountOnline-1){
			playerName=playersOnline[counter].getName();
			this.calcTotalTime(playerName);
			counter=counter + 1;
		}
		this.checkFileExistsAndCreate();
		File PluginFile=this.getDataFolder();
		this.checkJoinHashAndCreate(PluginFile);
		this.checkTotalHashAndCreate(PluginFile);
		File totalsFile=new File(PluginFile, "Totals.dat");
		File joinsFile=new File(PluginFile, "Joins.dat");
		FileOutputStream totalsFileOS=null;
		ObjectOutputStream totalsFileOOS=null;
		try {
			totalsFileOS=new FileOutputStream(totalsFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			totalsFileOOS=new ObjectOutputStream(totalsFileOS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			totalsFileOOS.writeObject(totalTimeMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			totalsFileOOS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream joinsFileOS=null;
		ObjectOutputStream joinsFileOOS=null;
		try {
			joinsFileOS=new FileOutputStream(joinsFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			joinsFileOOS=new ObjectOutputStream(joinsFileOS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			joinsFileOOS.writeObject(joinDateMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			joinsFileOOS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.getServer().getLogger().info("Last Login disabled");
	}

}
