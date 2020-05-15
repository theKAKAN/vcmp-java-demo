import commands.*
import com.maxorator.vcmp.java.plugin.integration.RootEventHandler
import com.maxorator.vcmp.java.plugin.integration.player.Player
import com.maxorator.vcmp.java.plugin.integration.server.Server
import com.maxorator.vcmp.java.plugin.integration.vehicle.Vehicle
import com.maxorator.vcmp.java.tools.events.DelegatingEventHandler

class ModeEventHandler(server: Server?) :
    RootEventHandler(server) {
    protected var eventHandler: DelegatingEventHandler = DelegatingEventHandler(server)
    override fun onServerInitialise(): Boolean {
        println("Server started!")
        return true
    }

    override fun onServerLoadScripts() {
        println("Scripts loaded!")
        eventHandler.add(this)
        eventHandler.add(TimerDemoCommands(server, eventHandler.timers))
        //eventHandler.add(SlowDemoCommands(server))
        eventHandler.add(AnnotationDemoCommands(server))
        eventHandler.add(RestrictedDemoCommands(server))
        eventHandler.add(vehicleCommands(server))
        eventHandler.takeOver()
    }

    override fun onServerUnloadScripts() {
        println("Scripts unloaded!")
    }

    override fun onIncomingConnection(
        name: String,
        password: String,
        ip: String
    ): String {
        return "$name!"
    }

    override fun onPlayerConnect(player: Player) {
        server.sendClientMessage(
            null,
            COLOUR_YELLOWISH,
            String.format("Player %s joined.", player.name)
        )
    }

    override fun onPlayerDisconnect(player: Player, reason: Int) {
        server.sendClientMessage(
            null,
            COLOUR_YELLOWISH,
            String.format("Player %s disconnected.", player.name)
        )
    }

    override fun onPlayerEnterVehicle(player: Player, vehicle: Vehicle, slot: Int) {
        server.sendClientMessage(
            player,
            COLOUR_YELLOWISH,
            String.format("Player %s entered vehicle %d at slot %d.", player.name, vehicle.id, slot)
        )
    }

    override fun onPlayerExitVehicle(player: Player, vehicle: Vehicle) {
        server.sendClientMessage(
            player,
            COLOUR_YELLOWISH,
            String.format("Player %s exited vehicle %d.", player.name, vehicle.id)
        )
    }

    override fun onVehicleExplode(vehicle: Vehicle) {
        server.sendClientMessage(
            null,
            COLOUR_YELLOWISH,
            String.format("Vehicle %d exploded.", vehicle.id)
        )
    }

    companion object {
        const val COLOUR_YELLOWISH = -0xab00
    }

}