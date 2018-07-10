package me.blayyke.cbot.script.entity;

import me.blayyke.cbot.script.ScriptEntity;
import me.blayyke.cbot.script.ScriptMentionable;
import me.blayyke.cbot.script.ScriptMessageReceiver;
import me.blayyke.cbot.script.ScriptNameHolder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.OffsetDateTime;

public class ScriptChannel implements ScriptEntity, ScriptNameHolder, ScriptMessageReceiver, ScriptMentionable {
    private TextChannel channel;

    public ScriptChannel(TextChannel channel) {
        this.channel = channel;
    }

    @Override
    public String getId() {
        return channel.getId();
    }

    @Override
    public String mention() {
        return channel.getAsMention();
    }

    @Override
    public void message(String message) {
        System.out.println("Sending message: " + message);
        channel.sendMessage(message).queue();
    }

    @Override
    public void message(MessageEmbed embed) {
        channel.sendMessage(embed).queue();
    }

    @Override
    public String getName() {
        return channel.getName();
    }

    public String getTopic() {
        return channel.getTopic();
    }

    public OffsetDateTime getCreationTime() {
        return channel.getCreationTime();
    }

    public ScriptMessage getMessage(String id) {
        Message message = channel.getMessageById(id).complete();
        return message == null ? null : new ScriptMessage(message);
    }
}