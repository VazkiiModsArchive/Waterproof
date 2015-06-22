package vazkii.waterproof;

import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "Waterproof", version = "1.0")
public class Waterproof {

	public static List<Block> waterproofBlocks = new ArrayList();
	
	static {
		waterproofBlocks.add(Blocks.redstone_wire);
		waterproofBlocks.add(Blocks.redstone_torch);
		waterproofBlocks.add(Blocks.unpowered_repeater);
		waterproofBlocks.add(Blocks.powered_repeater);
		waterproofBlocks.add(Blocks.unpowered_comparator);
		waterproofBlocks.add(Blocks.powered_comparator);
		waterproofBlocks.add(Blocks.rail);
		waterproofBlocks.add(Blocks.golden_rail);
		waterproofBlocks.add(Blocks.detector_rail);
		waterproofBlocks.add(Blocks.activator_rail);
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		String list = config.getString("waterproof", Configuration.CATEGORY_GENERAL, getBlocksAsList(), "The list of block names to be waterproof. Comma separated.");
		
		String[] tokens = list.split(",");
		waterproofBlocks.clear();
		for(String s : tokens) {
			Object obj = Block.blockRegistry.getObject(s);
			if(obj != null && obj instanceof Block)
				waterproofBlocks.add((Block) obj);
		}
		
		if(config.hasChanged())
			config.save();
	}
	
	static String getBlocksAsList() {
		String s = "";
		for(Block b : waterproofBlocks)
			s += Block.blockRegistry.getNameForObject(b) + ",";
		return s.substring(0, s.length() - 1);
	}
	
	public static boolean isWaterproof(World world, int x, int y, int z) {
		return waterproofBlocks.contains(world.getBlock(x, y, z));
	}
	
}

