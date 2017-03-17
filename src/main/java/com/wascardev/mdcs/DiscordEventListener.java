package com.wascardev.mdcs;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IMessage;

public class DiscordEventListener {

    private String syncedChannelID;

    DiscordEventListener(String syncedChannelID)
    {
        this.syncedChannelID = syncedChannelID;
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        if(message.getChannel().getID().equals(syncedChannelID) && !message.getContent().equals(""))
        {
            String prefix = "[Discord] <" + message.getAuthor().getName()  + "> ";
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            server.getPlayerList().sendMessage(new TextComponentString(prefix + message.getContent()));
        }
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new MinecraftEventListener(event.getClient().getChannelByID(syncedChannelID)));
    }
}
