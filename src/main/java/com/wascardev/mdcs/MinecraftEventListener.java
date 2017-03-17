package com.wascardev.mdcs;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import sx.blah.discord.handle.obj.IChannel;

public class MinecraftEventListener {

    private IChannel discordChannel;

    MinecraftEventListener(IChannel channel)
    {
        this.discordChannel = channel;
    }

    @SubscribeEvent
    public void onMessage(ServerChatEvent event)
    {
        try {
            discordChannel.sendMessage("**" + event.getUsername() + "** : " + event.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        try {
            discordChannel.sendMessage("**" + event.player.getDisplayNameString() + " is online**");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onLogoff(PlayerEvent.PlayerLoggedOutEvent event)
    {
        try {
            discordChannel.sendMessage("**" + event.player.getDisplayNameString() + " is offline**");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
