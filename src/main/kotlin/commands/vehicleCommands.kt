import com.maxorator.vcmp.java.plugin.integration.player.Player
import com.maxorator.vcmp.java.plugin.integration.server.Server
import com.maxorator.vcmp.java.plugin.integration.vehicle.Vehicle
import com.maxorator.vcmp.java.plugin.integration.vehicle.VehicleColours
import com.maxorator.vcmp.java.plugin.integration.vehicle.VehicleDamage
import com.maxorator.vcmp.java.tools.commands.Command
import com.maxorator.vcmp.java.tools.commands.CommandController

class vehicleCommands(protected val server: Server) : CommandController {
    override fun checkAccess(player: Player): Boolean {
        return true
    }

    @Command
    fun createVehicle(player: Player, modelId: Int) {
        val position = player.position
        position.x += 5.0f
        val vehicle = server.createVehicle(modelId, player.world, position, 0.0f, VehicleColours(1, 1))
        if (vehicle != null) {
            server.sendClientMessage(
                player,
                COLOUR_YELLOWISH,
                String.format("Vehicle %d created! YAY!", vehicle.id)
            )
        } else {
            server.sendClientMessage(player, COLOUR_YELLOWISH, "Could not create vehicle.")
        }
    }

    @Command
    fun getWorld(player: Player) {
        server.sendClientMessage(
            player,
            COLOUR_YELLOWISH,
            String.format("Your world is %d.", player.world)
        )
    }

    @Command
    fun setWorld(player: Player, world: Int) {
        player.world = world
        server.sendClientMessage(
            player,
            COLOUR_YELLOWISH,
            String.format("Set your world to %d.", world)
        )
    }

    @Command
    fun getPlayerVehicle(player: Player, target: Player) {
        val vehicle = target.vehicle
        if (vehicle != null) {
            server.sendClientMessage(
                player,
                COLOUR_YELLOWISH,
                String.format("Player %s is in vehicle %d.", target.name, vehicle.id)
            )
        } else {
            server.sendClientMessage(
                player,
                COLOUR_YELLOWISH,
                String.format("Player %s is not in a vehicle.", target.name)
            )
        }
    }

    @Command
    fun getVehicleHealth(player: Player, vehicle: Vehicle) {
        server.sendClientMessage(
            player,
            COLOUR_YELLOWISH,
            String.format("Vehicle %d health: %f.", vehicle.id, vehicle.health)
        )
    }

    @Command(usage = "<player> <vehicle> <slot> <makeroom> <warp>")
    fun putPlayerInVehicle(
        player: Player,
        target: Player,
        vehicle: Vehicle,
        slot: Int,
        makeRoom: Boolean,
        warp: Boolean
    ) {
        target.putInVehicle(vehicle, slot, makeRoom, warp)
        server.sendClientMessage(
            player,
            COLOUR_YELLOWISH,
            String.format("Sending player %s into vehicle %d.", target.name, vehicle.id)
        )
    }

    @Command
    fun getVehicleOccupant(player: Player, vehicle: Vehicle, slot: Int) {
        val occupant = vehicle.getOccupant(slot)
        if (occupant == null) {
            server.sendClientMessage(
                player,
                COLOUR_YELLOWISH,
                "That vehicle slot is not occupied."
            )
        } else {
            server.sendClientMessage(
                player,
                COLOUR_YELLOWISH,
                String.format("That slot is occupied by %s (%d).", occupant.name, occupant.id)
            )
        }
    }

    @Command
    fun getVehiclePosition(player: Player, vehicle: Vehicle) {
        val position = vehicle.position
        server.sendClientMessage(
            player,
            COLOUR_YELLOWISH,
            String.format("Vehicle %d is at (%f, %f, %f).", vehicle.id, position.x, position.y, position.z)
        )
    }

    @Command
    fun breakCar(player: Player, vehicle: Vehicle) {
        val damage = vehicle.damage
        damage.setDoorStatus(VehicleDamage.Door.Bonnet, VehicleDamage.DoorStatus.Flapping)
        damage.setTyreStatus(VehicleDamage.Tyre.LeftRear, VehicleDamage.TyreStatus.Flat)
        damage.setPanelStatus(VehicleDamage.Panel.Windscreen, VehicleDamage.PanelStatus.Damaged)
        vehicle.damage = damage
        server.sendClientMessage(player, COLOUR_YELLOWISH, "Broke that car.")
    }

    @Command
    fun gibAdmin(player: Player) {
        player.isAdmin = true
        server.sendClientMessage(player, -0x1, "Gabe admin")
    }

    companion object {
        const val COLOUR_YELLOWISH = -0xab00
    }

}