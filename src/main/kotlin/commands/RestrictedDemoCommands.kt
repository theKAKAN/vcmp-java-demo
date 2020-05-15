package commands

import com.maxorator.vcmp.java.plugin.integration.player.Player
import com.maxorator.vcmp.java.plugin.integration.server.Server
import com.maxorator.vcmp.java.tools.commands.Command
import com.maxorator.vcmp.java.tools.commands.CommandController


class RestrictedDemoCommands(protected val server: Server) : CommandController {
    override fun checkAccess(player: Player): Boolean {
        if (!player.isAdmin) {
            server.sendClientMessage(
                player,
                COLOUR_YELLOWISH,
                "You need to be an admin for this (/gibadmin)."
            )
            return false
        }
        return true
    }

    @Command
    fun getServerName(player: Player?) {
        server.sendClientMessage(
            player,
            COLOUR_YELLOWISH,
            String.format("Server name: %s", server.serverName)
        )
    }

    @Command
    fun setServerName(player: Player?, serverName: String?) {
        server.serverName = serverName
        server.sendClientMessage(
            player,
            COLOUR_YELLOWISH,
            String.format("Server name changed to: %s", serverName)
        )
    }

    @Command
    fun reload(player: Player?) {
        server.reloadScript()
    }

    companion object {
        const val COLOUR_YELLOWISH = -0xab00
    }

}
