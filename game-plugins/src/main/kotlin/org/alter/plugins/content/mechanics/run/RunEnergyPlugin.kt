package org.alter.plugins.content.mechanics.run

import org.alter.api.ext.*
import org.alter.game.*
import org.alter.game.model.*
import org.alter.game.plugin.*

class RunEnergyPlugin(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
        
    init {
        onLogin {
            player.timers[RunEnergy.RUN_DRAIN] = 1
        }

        onTimer(RunEnergy.RUN_DRAIN) {
            player.timers[RunEnergy.RUN_DRAIN] = 1
            RunEnergy.drain(player)
        }
        /**
         * Settings button.
         */
        onButton(interfaceId = 116, component = 71) {
            RunEnergy.toggle(player)
        }
    }
}
