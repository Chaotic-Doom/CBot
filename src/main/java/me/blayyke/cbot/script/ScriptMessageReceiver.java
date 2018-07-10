package me.blayyke.cbot.script;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

public interface ScriptMessageReceiver {
    void message(String message);

    void message(MessageEmbed embed);

    default void message(EmbedBuilder embed) {
        message(embed.build());
    }
}