package commands

import com.maxorator.vcmp.java.plugin.integration.generic.Colour
import com.maxorator.vcmp.java.plugin.integration.player.Player
import com.maxorator.vcmp.java.plugin.integration.server.Server
import com.maxorator.vcmp.java.tools.commands.Command
import com.maxorator.vcmp.java.tools.commands.CommandController
import com.maxorator.vcmp.java.tools.commands.NullIfNotFound
import com.maxorator.vcmp.java.tools.commands.PartialMatch


class AnnotationDemoCommands(protected val server: Server) : CommandController {
    override fun checkAccess(player: Player): Boolean {
        return true
    }

    @Command(name = "renamed")
    fun methodName(player: Player?) {
        // Command name is usually the same as method name (in lowercase), but this can be changed like done above
        server.sendClientMessage(
            player,
            RESPONSE_COLOUR,
            "This command has a custom name that differs from method name."
        )
    }

    @Command(usage = "<random number>")
    fun lottery(player: Player?, number: Int) {
        // A basic usage text is generated automatically, but a custom one is probably better
        server.sendClientMessage(
            player,
            RESPONSE_COLOUR,
            String.format(
                "Good job on finally entering the correct parameters. Oh and %d is not a winning number",
                number
            )
        )
    }

    @Command
    fun findDefault(player: Player?, target: Player) {
        // By default, finding a player requires the format #<id> or <exact name>
        server.sendClientMessage(
            player,
            RESPONSE_COLOUR,
            String.format("Player '%s' found.", target.name)
        )
    }

    @Command
    fun findNoError(player: Player?, @NullIfNotFound target: Player?) {
        // By default, an error is automatically shown if the entity is not found, with this annotation you will instead get null
        if (target == null) {
            server.sendClientMessage(
                player,
                RESPONSE_COLOUR,
                "No such guy lives here."
            )
        } else {
            server.sendClientMessage(
                player,
                RESPONSE_COLOUR,
                String.format("Yep, found %s.", target.name)
            )
        }
    }

    @Command
    fun findPartial(player: Player?, @PartialMatch target: Player) {
        // This annotation does not require an exact match, but gives an error when there are several matches
        server.sendClientMessage(
            player,
            RESPONSE_COLOUR,
            String.format("The dude called %s seems close enough.", target.name)
        )
    }

    companion object {
        protected val RESPONSE_COLOUR = Colour(200, 255, 150)
    }

}
