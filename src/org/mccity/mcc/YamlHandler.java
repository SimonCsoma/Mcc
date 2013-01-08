package org.mccity.mcc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mccity.mcc.plot.Plot;

public class YamlHandler {

	File configFile;
	File plotsFile;
	public FileConfiguration config;
	public FileConfiguration plots;
	public Mcc plugin;
	
	public YamlHandler(Mcc plugin){
		
		this.plugin = plugin;
		configFile = new File(plugin.getDataFolder(), "config.yml");
		plotsFile = new File(plugin.getDataFolder(), "plots.yml");
		
	}
	
	/*
	 * Initializes the yaml files if they are not present
	 */
	
	public void firstRun() throws Exception {
        if(!configFile.exists()){                        
            configFile.getParentFile().mkdirs();    
            Bukkit.getLogger().info("No config file found, generating one");
            copy(plugin.getResource("config.yml"), configFile); 
        }
        if(!plotsFile.exists()){
            plotsFile.getParentFile().mkdirs();
            Bukkit.getLogger().info("No plots file found, generating one");
            copy(plugin.getResource("plots.yml"), plotsFile);
        }
    }

   /*
    * if the yamls are missing this will copy the original files to the MCC folder
    */
    public void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     * in here, each of the FileConfigurations loaded the contents of yamls
     *  found at the /plugins/<pluginName>/*yml.
     * needed at onEnable() after using firstRun();
     * can be called anywhere if you need to reload the yamls.
     */
    public void load() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile); //loads the contents of the File to its FileConfiguration
            plots = YamlConfiguration.loadConfiguration(plotsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * save all FileConfigurations to its corresponding File
     * optional at onDisable()
     * can be called anywhere if you have *.set(path,value) on your methods
     */
    public void save() {
        try {
        	config.save(configFile); //saves the FileConfiguration to its File
            plots.save(plotsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /*
     * Returns all the city names in a List
     * if the file stream plots == null 
     * it try's to reload the file stream
     */
	public List<String> getCityNames() {
		
		if (plots == null){
			load();
		}
		List<String> cityNames;
		cityNames = plots.getStringList("citys.MCC");
		
		return cityNames;
	}
	
	/*
	 * Returns the Map of lists containing the plots by world
	 * If nothing is found its assumed no plots.yml is present!
	 */
	protected Map<String,Plot> loadPlots(World world){
		if(plots == null)
			load();
		@SuppressWarnings("unchecked")
		Map<String,Plot> plotMap = (Map<String,Plot>) plots.getList("plots." + world.getName());
		if (plotMap == null){
			try {
				firstRun();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		return plotMap;
		
	}
	
	protected void savePlots(Map<String,List<Plot>> plotMap, String world){
		if(plots == null){
			load();
		}
		plots.set("plots." + world, plotMap);
		
	}
}
