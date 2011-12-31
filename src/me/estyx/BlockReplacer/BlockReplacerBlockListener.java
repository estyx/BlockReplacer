package me.estyx.BlockReplacer;

/* Internal */
import me.estyx.BlockReplacer.BlockReplacer.ReplacerType;

/* Bukkit */
import org.bukkit.block.*;
import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

public class BlockReplacerBlockListener extends BlockListener
{
	private final BlockReplacer plugin;

    public BlockReplacerBlockListener(final BlockReplacer plugin)
    {
        this.plugin = plugin;
    }
    
    public void onSignChange(SignChangeEvent event)
    {
    	if((event.getLine(0).equalsIgnoreCase(plugin.signIdentifier) ||
    			event.getLine(0).equalsIgnoreCase(ChatColor.RED + plugin.signIdentifier) ||
    			event.getLine(0).equalsIgnoreCase(ChatColor.GREEN + plugin.signIdentifier)))
       	{
    		/* Permissions check */
    		if(!plugin.hasPermissions(event.getPlayer(), "blockreplacer.create"))
    		{
    			if(plugin.debug)
    				System.out.println("[BlockReplacer] " + event.getPlayer().getDisplayName() + " does not have sufficient permissions create sign");
    			
    			/* This player obviously doesnt have the credentials to create own BlockReplacer signs, so lets show
    			 * him a message on the sign and prevent abuse by removing the identifier */
    			event.setLine(0, ChatColor.RED + "Denied");
    			event.getBlock().getState().update(true);
    			return;
    		}
    		
    		/* Check if the sign is powered by redstone or not */
    		if(event.getBlock().isBlockPowered() || event.getBlock().isBlockIndirectlyPowered())
    			event.setLine(0, ChatColor.GREEN + plugin.signIdentifier);
    		else
    			event.setLine(0, ChatColor.RED + plugin.signIdentifier);
   			
    		if (plugin.debug)
   				System.out.println("[BlockReplacer] sign created!");
   			return;
       	}
    	
    	if(plugin.enablePresets && (event.getLine(0).equalsIgnoreCase(plugin.presetIdentifier) ||
    			event.getLine(0).equalsIgnoreCase(ChatColor.RED + plugin.presetIdentifier) ||
    			event.getLine(0).equalsIgnoreCase(ChatColor.GREEN + plugin.presetIdentifier)))
    	{
    		/* Permissions check */
    		if(!plugin.hasPermissions(event.getPlayer(), "blockreplacer.create"))
    		{
    			if(plugin.debug)
    				System.out.println("[BlockReplacer] " + event.getPlayer().getDisplayName() + " does not have sufficient permissions create sign");
    			
    			/* This player obviously doesnt have the credentials to create own BlockReplacer signs, so lets show
    			 * him a message on the sign and prevent abuse by removing the identifier */
    			event.setLine(0, "");
    			event.setLine(1, ChatColor.RED + "Access");
    			event.setLine(2, ChatColor.RED + "Denied");
    			event.setLine(3, "");
    			event.getBlock().getState().update(true);
    			return;
    		}
    		
    		if(event.getBlock().isBlockPowered() || event.getBlock().isBlockIndirectlyPowered())
    			event.setLine(0, ChatColor.GREEN + plugin.presetIdentifier);
    		else
    			event.setLine(0, ChatColor.RED + plugin.presetIdentifier);
   			
    		if (plugin.debug)
   				System.out.println("[BlockReplacer] sign created!");
   			return;
    	}
    }
    
    public void onBlockRedstoneChange(BlockRedstoneEvent event)
    {
    	if(!this.plugin.signHandler.isSign(event.getBlock()))
    		return;
    	
    	Sign signObject = (Sign) event.getBlock().getState();
    	
    	ReplacerType result = plugin.signHandler.getReplaceSignType(signObject);
    	if(result == ReplacerType.TYPE_FAILED)
    		return;
    	
    	switch(result)
    	{
	    	case TYPE_REGULAR_ON:
				if(!(event.getBlock().isBlockPowered() || event.getBlock().isBlockIndirectlyPowered()))
					plugin.signHandler.powerOff(event.getBlock());
				break;
			
			case TYPE_REGULAR_OFF:
				if(event.getBlock().isBlockPowered() || event.getBlock().isBlockIndirectlyPowered())
					plugin.signHandler.powerOn(event.getBlock());
				break;
    		
			case TYPE_PRESET_ON:
    			if(plugin.enablePresets)
    				if(!(event.getBlock().isBlockPowered() || event.getBlock().isBlockIndirectlyPowered()))
    					plugin.signHandler.powerOffPreset(event.getBlock());
    			break;
    		
			case TYPE_PRESET_OFF:
    			if(plugin.enablePresets)
    				if(event.getBlock().isBlockPowered() || event.getBlock().isBlockIndirectlyPowered())
    					plugin.signHandler.powerOnPreset(event.getBlock());
    			break;
    	}
    }
}
