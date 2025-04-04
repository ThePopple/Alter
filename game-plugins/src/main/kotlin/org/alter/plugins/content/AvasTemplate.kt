package org.alter.plugins.content

import org.alter.api.ext.*
import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.game.type.interfacedsl.*
import java.awt.Color

class AvasTemplate(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
    init {
        Interface(AvasDevices) {
            destination(InterfaceDestination.MAIN_SCREEN)
            component("Ava's Attractor", 2) {
                click { _,   option, _ ->
                    player.message("Ava's Attractor $option")
                }
            }
            component("Ava's Attractor Requirement Description", 6) {
                setText("<str>999 Coins</str>")
                setColor(Color.YELLOW)
            }

            component("Ava's Accumulator", 7) {
                click { _, option, _ ->
                    player.message("Ava's Accumulator $option")
                }
            }
            component("Ava's Accumulator Requirement Description", 11) {
                setText("<str>999 coins</str> OR an <str>Attractor</str>,<br>AND <str>75 steel arrows</str>")
            }
            component("Ava's Assembler", 12) {
                click { _, option, _ ->
                    player.message("Ava's Assembler $option")
                }
            }
            component("Ava's Accumulator Requirement Description", 16) {
                setText("<str>4999 coins</str> OR an <str>Accumulator</str>,<br>AND <str>75 mithril arrows</str>,<br>AND <str>the head of Vorkath</str>")
            }
            onOpen {
                println("onOpen Trigger")
            }
            onClose {
                player.message("onClose Triggered")
            }
        }
        onCommand("tin") {
            player.message("AvasDevices")
            player.openInterface(AvasDevices)
        }
    }
}

object AvasDevices : OverlayInterface() {
    override val entry = "interface.avas_devices"
    // @TODO Either add hash check from here or during boot check for changes
}





