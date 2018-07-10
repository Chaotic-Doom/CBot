package me.blayyke.cbot.script.entity;

import me.blayyke.cbot.script.ScriptEntity;
import net.dv8tion.jda.core.entities.Message;

public class ScriptMessage implements ScriptEntity {
    private final Message message;

    public ScriptMessage(Message message) {
        this.message = message;
    }

    public void pin() {
        if (!isPinned()) message.pin().queue();
    }

    public void unpin() {
        if (isPinned()) message.unpin().queue();
    }

    public boolean isPinned() {
        return message.isPinned();
    }

    public String getContent() {
        return message.getContentRaw();
    }

    ScriptMember getAuthor() {
        return new ScriptMember(message.getMember());
    }

    public boolean mentionsEveryone() {
        return message.mentionsEveryone();
    }

    @Override
    public String getId() {
        return message.getId();
    }
}