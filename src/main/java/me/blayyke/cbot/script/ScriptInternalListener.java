package me.blayyke.cbot.script;

import me.blayyke.cbot.script.event.ScriptEvent;
import me.blayyke.cbot.script.event.ScriptMemberJoinEvent;
import me.blayyke.cbot.script.event.ScriptMemberLeaveEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.*;

public class ScriptInternalListener extends ListenerAdapter {
    private static final ScriptInternalListener instance;
    private Map<Class<? extends ScriptEvent>, List<ScriptCustomListener>> listenerMap = new HashMap<>();

    private ScriptInternalListener() {
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        fireEvent(ScriptMemberLeaveEvent.class);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        fireEvent(ScriptMemberJoinEvent.class);
    }

    private void fireEvent(Class<? extends ScriptEvent> event) {
        List<ScriptCustomListener> scriptCustomListeners = listenerMap.get(event);
        if (scriptCustomListeners != null)
            for (ScriptCustomListener scriptCustomListener : scriptCustomListeners) scriptCustomListener.fireEvent();
    }

    @SuppressWarnings("unchecked")
    public void registerListener(ScriptCustomListener listener) {
        List<ScriptCustomListener> scriptCustomListeners = listenerMap.computeIfAbsent((Class<? extends ScriptEvent>) (Object) listener.getEvent().getClass(), k -> new ArrayList<>());
        scriptCustomListeners.add(listener);
    }

    public void unregisterListener(Guild guild, Class<? extends ScriptEvent> event) {
        if (listenerMap.containsKey(event)) {
            Iterator<ScriptCustomListener> it = listenerMap.get(event).iterator();
            while (it.hasNext()) {
                ScriptCustomListener scriptCustomListener = it.next();
                if (scriptCustomListener.getGuild().getId().equals(guild.getId())) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public static ScriptInternalListener getInstance() {
        return instance;
    }

    static {
        instance = new ScriptInternalListener();
    }
}