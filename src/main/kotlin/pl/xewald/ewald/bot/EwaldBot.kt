package pl.xewald.ewald.bot

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game
import pl.xewald.ewald.bot.command.admin.BroadcastCommand
import pl.xewald.ewald.bot.command.useful.PollCommand
import pl.xewald.ewald.bot.command.bot.BotCommand
import pl.xewald.ewald.bot.command.bot.HelpCommand
import pl.xewald.ewald.bot.command.game.*
import pl.xewald.ewald.bot.command.CommandManager
import pl.xewald.ewald.bot.command.`fun`.*
import pl.xewald.ewald.bot.command.useful.*
import pl.xewald.ewald.bot.config.EwaldBotConfig
import pl.xewald.ewald.bot.listener.*
import pl.xewald.ewald.bot.util.EwaldBotException

class EwaldBot(val config: EwaldBotConfig) {

    lateinit var jda: JDA
        private set

    var commandManager = CommandManager()

    var running = false
        private set

    fun start() {
        if (running)
            throw EwaldBotException("Bot is already running!")
        val token = config.token
        if (token.isEmpty() || token == "TOKEN")
            throw EwaldBotException("You have to set token in configuration file!")
        jda = JDABuilder.createDefault(token)
                .addEventListener(JoinListener(this))
                .addEventListener(LeaveListener(this))
                .addEventListener(GuildListener(this))
                .addEventListener(BanListener(this))
                .addEventListener(MessageListener(this))
                .setAudioEnabled(false)
                .setAutoReconnect(true)
                .setStatus(OnlineStatus.ONLINE)
                .setGame(Game.streaming("Wpisz !help | v1.2", "https://www.twitch.tv/boleknowak"))
                .buildBlocking()
        commandManager.add(HelpCommand(this))
        commandManager.add(ChannelCommand(this))
        commandManager.add(IllegalCommand(this))
        commandManager.add(UserCommand(this))
        commandManager.add(BotCommand(this))
        commandManager.add(ServerCommand(this))
        commandManager.add(BroadcastCommand(this))
        commandManager.add(MCServerCommand(this))
        commandManager.add(HiveMCPlayerCommand(this))
        commandManager.add(IllegalCommand(this))
        commandManager.add(YesNoCommand(this))
        commandManager.add(EmbedCommand(this))
        commandManager.add(HugCommand(this))
        commandManager.add(HitCommand(this))
        commandManager.add(AvatarCommand(this))
        commandManager.add(RPSCommand(this))
        commandManager.add(PollCommand(this))
        commandManager.add(CryptocurrencyCommand(this))
        running = true
    }

    fun stop() {
        if (!running)
            throw EwaldBotException("Bot is not running!")
        running = false
        jda.shutdown()
    }

}
