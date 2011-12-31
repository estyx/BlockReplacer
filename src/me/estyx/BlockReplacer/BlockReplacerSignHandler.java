package me.estyx.BlockReplacer;

/* Internal */
import me.estyx.BlockReplacer.BlockReplacer.ReplacerType;

/* Bukkit */
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.Location;

public class BlockReplacerSignHandler
{
	private final BlockReplacer plugin;
	
    public BlockReplacerSignHandler(BlockReplacer instance)
    {
        plugin = instance;
    }
    
    public void powerOnPreset(Block block)
    {
    	String preset;
    	int    length;
		int    position;
    }
    
    public void powerOffPreset(Block block)
    {
    	String preset;
    	int    length;
		int    position;
    }
    
    public void powerOn(Block block)
    {
    	Sign signObject = (Sign) block.getState();
    	
    	int length;
		int position;
		int blockon;
		int blockon_dmg;
		
		try
		{
			/* Line 1: <y-position>[;<length>]*/
			if(signObject.getLine(1).contains(";"))
			{
				String[] str;
				str = signObject.getLine(1).split(";");
				position = Integer.parseInt(str[0]);
				length   = Integer.parseInt(str[1]);
			} else
			{
				position = Integer.parseInt(signObject.getLine(1));
				length   = 1;
			}
			if(length < 1)
				length = 1;
			if(length > 32)
				length = 32;
			
			/* Line 2: <blockid-on>[:<data>] */
			if(signObject.getLine(2).contains(":"))
			{
				String[] str;
				str = signObject.getLine(2).split(":");
				blockon     = Integer.parseInt(str[0]);
				blockon_dmg = Integer.parseInt(str[1]);
			} else
			{
				blockon     = Integer.parseInt(signObject.getLine(2));
				blockon_dmg = 0;
			}
			if(blockon_dmg < 0)
				blockon_dmg = 0;
		
		} catch (NumberFormatException e)
		{
			System.err.println("Caught NumberFormatException: " + e.getMessage());
			return;
		}
		
		Location location = block.getLocation();
		World w = location.getWorld();
		location.setY(location.getY() + position);
		
		if(position >= 0)
		{
			for(int i=position; i<length+position; i++)
			{
				Block b = (Block) w.getBlockAt(location);
				b.setTypeIdAndData(blockon, (byte) blockon_dmg, true);
				b.getState().update(true);
				location.setY(location.getY() + 1);
			}
		} else
		{
			for(int i=position; i>position-length; i--)
			{
				Block b = (Block) w.getBlockAt(location);
				b.setTypeIdAndData(blockon, (byte) blockon_dmg, true);
				b.getState().update(true);
				location.setY(location.getY() - 1);
			}
		}
				
		
		signObject.setLine(0, ChatColor.GREEN + plugin.signIdentifier);
		signObject.update(true);
    	return;
    }
    
    public void powerOff(Block block)
    {
    	Sign signObject = (Sign) block.getState();
    	
    	int length;
		int position;
		int blockoff;
		int blockoff_dmg;
		
		try
		{
			/* Line 1: <y-position>[;<length>]*/
			if(signObject.getLine(1).contains(";"))
			{
				String[] str;
				str = signObject.getLine(1).split(";");
				position = Integer.parseInt(str[0]);
				length   = Integer.parseInt(str[1]);
			} else
			{
				position = Integer.parseInt(signObject.getLine(1));
				length   = 1;
			}
			if(length < 1)
				length = 1;
			if(length > 32)
				length = 32;
			
			/* Line 3: <blockid-off>[:<data>] */
			if(signObject.getLine(3).contains(":"))
			{
				String[] str;
				str = signObject.getLine(3).split(":");
				blockoff     = Integer.parseInt(str[0]);
				blockoff_dmg = Integer.parseInt(str[1]);
			} else
			{
				blockoff     = Integer.parseInt(signObject.getLine(3));
				blockoff_dmg = 0;
			}
			if(blockoff_dmg < 0)
				blockoff_dmg = 0;
		} catch (NumberFormatException e)
		{
			System.err.println("Caught NumberFormatException: " + e.getMessage());
			return;
		}
		
		Location location = block.getLocation();
		World w = location.getWorld();
		location.setY(location.getY() + position);
		
		if(position >= 0)
		{
			for(int i=position; i<length+position; i++)
			{
				Block b = (Block) w.getBlockAt(location);
				b.setTypeIdAndData(blockoff, (byte) blockoff_dmg, true);
				b.getState().update(true);
				location.setY(location.getY() + 1);
			}
		} else
		{
			for(int i=position; i>position-length; i--)
			{
				Block b = (Block) w.getBlockAt(location);
				b.setTypeIdAndData(blockoff, (byte) blockoff_dmg, true);
				b.getState().update(true);
				location.setY(location.getY() - 1);
			}
		}
		
		signObject.setLine(0, ChatColor.RED + plugin.signIdentifier);
		signObject.update(true);
    	return;
    }
    
    public boolean isSign(Block b)
    {
    	if (b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
    		return true;
    	else
    		return false;
    }
    
    public ReplacerType getReplaceSignType(Sign signObject)
    {
    	if(signObject.getLine(0).equals(ChatColor.GREEN + plugin.signIdentifier))
    		return ReplacerType.TYPE_REGULAR_ON;
    	if(signObject.getLine(0).equals(ChatColor.RED + plugin.signIdentifier))
    		return ReplacerType.TYPE_REGULAR_OFF;
    	
    	if(signObject.getLine(0).equals(ChatColor.GREEN + plugin.presetIdentifier))
    		return ReplacerType.TYPE_PRESET_ON;
    	if(signObject.getLine(0).equals(ChatColor.RED + plugin.presetIdentifier))
    		return ReplacerType.TYPE_PRESET_OFF;
    	
    	return ReplacerType.TYPE_FAILED;
    }
}

