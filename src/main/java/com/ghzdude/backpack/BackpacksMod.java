package com.ghzdude.backpack;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BackpacksMod.MODID,
     name = "Ghzdude's Backpacks",
     version = "0.1",
     acceptedMinecraftVersions = "[1.12.2,1.13)",
     dependencies = "required:forge; required-after:modularui@[2.3.1,)")
public class BackpacksMod {
    public static final String MODID = "backpacks";
    public static final Logger logger = LogManager.getLogger(MODID);
}
