package me.blayyke.cbot.command;

import me.blayyke.cbot.DataManager;
import me.blayyke.cbot.MiscUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;

public class CommandCustom extends AbstractCommand {
    @Override
    String getName() {
        return "custom";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        if (args.length < 2) {
            event.getChannel().sendMessage("Please specify an action and a custom command!").queue();
            return;
        }
        CustomCommandExecutor command = DataManager.getInstance().getGuildData(event.getGuild()).getCommand(args[1]);
        if (args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("create")) {
            if (command != null) {
                event.getChannel().sendMessage("A command by that name already exists!").queue();
                return;
            }
            CustomCommandExecutor newCmd = new CustomCommandExecutor(event.getGuild(), args[1].toLowerCase());
            newCmd.setCreatorId(event.getAuthor().getIdLong());

            DataManager.getInstance().getGuildData(event.getGuild()).createCommand(newCmd);
            event.getChannel().sendMessage("Created new command `" + args[1] + "`.").queue();
            return;
        }
        if (command == null) {
            event.getChannel().sendMessage("`" + args[1].replace("`", "\\`") + "` is not a valid custom command.").queue();
            return;
        }

        System.out.println(Arrays.toString(args));
        boolean update = args.length > 2;
        String input = MiscUtils.joinStringArray(args, " ", 2);
        switch (args[0].toLowerCase()) {
            case "a":
            case "action":
                if (!update) {
                    if (MiscUtils.actionIsUrl(command.getAction()))
                        event.getChannel().sendMessage("The action URL is set to `" + command.getAction() + "`.").queue();
                    else
                        event.getChannel().sendMessage("```js\n" + (command.getAction().length() > 1992 ? command.getAction().substring(0, 1992) : command.getAction()) + "```").queue();
                } else {
                    command.setAction(input);
                    event.getChannel().sendMessage("Action updated.").queue();
                }
                break;
            case "d":
            case "desc":
            case "description":
                if (!update) {
                    event.getChannel().sendMessage("The description is set to `" + command.getDescription().replace("`", "\\`") + "`.").queue();
                    return;
                }
                command.setDescription(input);
                event.getChannel().sendMessage("Description updated.").queue();
                break;
            case "l":
            case "list":
            case "commands":
                throw new NotImplementedException();
            case "update":
                if (!MiscUtils.actionIsUrl(command.getAction())) {
                    event.getChannel().sendMessage("This action is only needed for actions set to a URL.").queue();
                    return;
                }
                command.setCode(MiscUtils.getActionFromUrl(command.getAction()));
                event.getChannel().sendMessage("Updated code for command from " + command.getAction()).queue();
                break;
            default:
                event.getChannel().sendMessage("Unknown action! Actions: `action, create, desc, delete, list, update`.").queue();
        }
    }
}