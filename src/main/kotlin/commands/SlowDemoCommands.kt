package commands

import com.maxorator.vcmp.java.plugin.integration.generic.Colour
import com.maxorator.vcmp.java.plugin.integration.player.Player
import com.maxorator.vcmp.java.plugin.integration.server.Server
import com.maxorator.vcmp.java.tools.commands.Command
import com.maxorator.vcmp.java.tools.commands.CommandController


class SlowDemoCommands(protected val server: Server) : CommandController {
    override fun checkAccess(player: Player): Boolean {
        return true
    }

    private fun doSomethingSlow() {
        // Do not actually use this as timers, use it when running something like an SQL query that might not be very fast
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            Thread.interrupted()
        }
    }

    @Command
    fun slow(player: Player) {
        server.sendClientMessage(player, RESPONSE_COLOUR, "Running a slow thing.")

        // In a different thread, you cannot normally call server methods (including player/vehicle/etc)
        Thread(Runnable {
            doSomethingSlow()

            // In this block, you can call server functions (server main thread pauses while this is running)
            try {
                server.sync().use { _ ->
                    if (player.isValid) {
                        server.sendClientMessage(
                            player,
                            RESPONSE_COLOUR,
                            "The slow thing has finished."
                        )
                    } else {
                        server.sendClientMessage(
                            null,
                            RESPONSE_COLOUR,
                            "The guy whose query I was running has left."
                        )
                    }
                }
            } catch (ignored: Exception) {
                System.err.println("Slow job finished when scripts were unloaed.")
            }
        }).start()
    }

    companion object {
        protected val RESPONSE_COLOUR = Colour(200, 255, 200)
    }

}