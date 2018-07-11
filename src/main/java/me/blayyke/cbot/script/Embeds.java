package me.blayyke.cbot.script;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;

public class Embeds {
    private Guild guild;

    public Embeds(Guild guild) {
        this.guild = guild;
    }

    public EmbedBuilder createEmbed() {
        return new EmbedBuilder();
    }
}