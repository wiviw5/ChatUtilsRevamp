package com.github.wiviw5.chatutilsrevamp;

import com.github.wiviw5.chatutilsrevamp.events.onChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "chatutilsrevamp", useMetadata=true)
public class ChatUtils {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Initializing chat Listener.");
        MinecraftForge.EVENT_BUS.register(new onChatEvent());
    }
}
