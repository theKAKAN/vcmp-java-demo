package commands

import com.maxorator.vcmp.java.plugin.integration.generic.Colour
import com.maxorator.vcmp.java.plugin.integration.player.Player
import com.maxorator.vcmp.java.plugin.integration.server.Server
import com.maxorator.vcmp.java.tools.commands.Command
import com.maxorator.vcmp.java.tools.commands.CommandController
import com.maxorator.vcmp.java.tools.timers.TimerHandle
import com.maxorator.vcmp.java.tools.timers.TimerRegistry


class TimerDemoCommands(
    protected val server: Server,
    protected val timerRegistry: TimerRegistry
) :
    CommandController {
    override fun checkAccess(player: Player): Boolean {
        return true
    }

    @Command
    fun pingMe(player: Player) {
        var handle = player.getData("pinger", TimerHandle::class.java)
        if (handle != null) {
            server.sendClientMessage(
                player,
                RESPONSE_COLOUR,
                "You are already being pinged."
            )
        } else {
            handle = timerRegistry.register(
                true, 5000
            ) { server.sendClientMessage(player, RESPONSE_COLOUR, "PING!") }
            player.setData("pinger", handle)
            server.sendClientMessage(
                player,
                RESPONSE_COLOUR,
                "Pinging you every 5 seconds."
            )
        }
    }

    @Command
    fun stopPing(player: Player) {
        val handle = player.getData("pinger", TimerHandle::class.java)
        if (handle == null) {
            server.sendClientMessage(player, RESPONSE_COLOUR, "You are not being pinged.")
        } else {
            handle.cancel()
            server.sendClientMessage(player, RESPONSE_COLOUR, "Stopping your pings.")
        }
    }

    companion object {
        protected val RESPONSE_COLOUR = Colour(255, 200, 200)
    }

}
