package com.wascardev.mdcs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Mod(modid = MinecraftDiscordChatSyncer.MODID, version = MinecraftDiscordChatSyncer.VERSION)
public class MinecraftDiscordChatSyncer {
    public static final String MODID = "minecraft-discord-chat-syncer";
    public static final String VERSION = "1.0";

    private Config config = null;
    private IDiscordClient discordClient = null;

    @SideOnly(Side.SERVER)
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File configDirectory = event.getModConfigurationDirectory();
        if (!configDirectory.exists() && !configDirectory.mkdirs())
            throw new IOException("Cannot to create directory : " + configDirectory.getAbsolutePath());

        File jsonConfigFile = new File(configDirectory, "minecraftDiscordChatSyncer.json");
        if (!jsonConfigFile.exists()) {
            if (jsonConfigFile.createNewFile()) {
                this.config = new Config();
                Files.write(jsonConfigFile.toPath(), gson.toJson(config).getBytes());
            } else
                throw new IOException("Cannot to create file : " + jsonConfigFile.getAbsolutePath());
        } else
            this.config = gson.fromJson(new String(Files.readAllBytes(jsonConfigFile.toPath())), Config.class);
    }

    @SideOnly(Side.SERVER)
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if(!config.getToken().equals("") && !config.getSyncedChannelID().equals("")) {
            ClientBuilder clientBuilder = new ClientBuilder();
            clientBuilder.withToken(this.config.getToken());
            try {
                this.discordClient = clientBuilder.login();
                this.discordClient.getDispatcher().registerListener(new DiscordEventListener(config.getSyncedChannelID()));
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        }
    }

    @SideOnly(Side.SERVER)
    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        if(discordClient != null && discordClient.isLoggedIn()) {
            try {
                this.discordClient.logout();
            } catch (DiscordException e) {
                e.printStackTrace();
            }
        }
    }
}
