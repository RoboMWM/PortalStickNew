package net.tangentmc.portalStick.commands;

import org.bukkit.entity.Player;

import net.tangentmc.nmsUtils.utils.Utils;
import net.tangentmc.portalStick.components.Region;
import net.tangentmc.portalStick.utils.RegionSetting;



public class FlagCommand extends BaseCommand {

	public FlagCommand() {
		super("flag", 3, "<region> <flag> <value> <- flag a region", false);
	}
	
	public boolean execute() {
		
		Region editRegion = plugin.getRegionManager().getRegion(args[0]);
		if (editRegion == null) {
			Utils.sendMessage(sender, plugin.getI18n().getString("RegionNotFound", playerName, args[0]));
			return true;
		}
		
		for (RegionSetting setting : RegionSetting.values()) {
			if (setting.getYaml().equalsIgnoreCase(args[1]) && setting.getEditable()) {
				Object old = editRegion.settings.remove(setting);
				try {
					
					if (setting.getDefault() instanceof Integer)
						editRegion.settings.put(setting, Integer.parseInt(args[2]));
					else if (setting.getDefault() instanceof Double)
						editRegion.settings.put(setting, Double.parseDouble(args[2]));
					else if (setting.getDefault() instanceof Boolean)
						editRegion.settings.put(setting, Boolean.parseBoolean(args[2]));
					else
						editRegion.settings.put(setting, args[2]);
					
					Utils.sendMessage(sender, plugin.getI18n().getString("RegionUpdated", playerName, editRegion.name));
					plugin.getConfiguration().saveAll();
				} catch (Throwable t) {
					Utils.sendMessage(sender, plugin.getI18n().getString("InvalidRegionFlagValue", playerName, setting.getYaml()));
					editRegion.settings.put(setting, old);
				}
				return true;
			}
		}
		Utils.sendMessage(sender, plugin.getI18n().getString("RegionUpdated", playerName, args[1]));
		StringBuilder sb = new StringBuilder("&c");
		for (RegionSetting setting : RegionSetting.values())
			if (setting.getEditable()) 
				sb.append("&c").append(setting.getYaml()).append("&7, ");
		sb.delete(sb.length() - 2, sb.length());
		Utils.sendMessage(sender, sb.toString());
		return true;
	}
	
	public boolean permission(Player player) {
		return plugin.hasPermission(player, plugin.PERM_ADMIN_REGIONS);
	}

}
