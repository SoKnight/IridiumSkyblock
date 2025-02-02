package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Command which display plugin information to the user.
 */
public class AboutCommand extends Command {

    /*
    Please don't add yourself to this list, if you contribute enough I (Peaches) will add you.
    */
    private final List<String> contributors = Arrays.asList("SoKnight", "das_", "SlashRemix");

    /**
     * The default constructor.
     */
    public AboutCommand() {
        super(Collections.singletonList("about"), "Display plugin info", "", false, Duration.ZERO);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Display plugin information to the user.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(StringUtils.color("&7[Информация о IridiumSkyblock]"));
        sender.sendMessage(StringUtils.color("&7Версия: &b" + IridiumSkyblock.getInstance().getDescription().getVersion() + " (Неофициальная)"));
        sender.sendMessage(StringUtils.color("&7Оригинальный автор: &bPeaches_MLG"));
        sender.sendMessage(StringUtils.color("&7Другие разработчики: &b" + String.join(", ", contributors)));
        sender.sendMessage(StringUtils.color("&8Используется собственный форк проекта StarMC."));
        return true;
    }

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param command       The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        // We currently don't want to tab-completion here
        // Return a new List, so it isn't a list of online players
        return Collections.emptyList();
    }

}
