package me.blayyke.cbot;

import gnu.trove.map.TLongObjectMap;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.utils.MiscUtil;

public class DataManager {
    private static final DataManager instance;
    private TLongObjectMap<GuildData> guildStorageMap = MiscUtil.newLongMap();

    public static DataManager getInstance() {
        return instance;
    }

    public GuildData getGuildData(Guild guild) {
        if (!guildStorageMap.containsKey(guild.getIdLong()))
            guildStorageMap.put(guild.getIdLong(), new GuildData(guild));
        return guildStorageMap.get(guild.getIdLong());
    }

    static {
        instance = new DataManager();
    }
}