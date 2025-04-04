package org.alter.plugins.content.interfaces.gameframe

import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.plugin.KotlinPlugin
import org.alter.game.plugin.PluginRepository
import org.alter.game.type.interfacedsl.Interface
import org.alter.game.type.interfacedsl.InterfaceDestination
import org.alter.game.type.interfacedsl.OverlayInterface

class XpDropsWindow(
    r: PluginRepository,
    world: World,
    server: Server
) : KotlinPlugin(r, world, server) {
    init {
        Interface(XpDropWindow) {
            destination(InterfaceDestination.XP_COUNTER)
        }
    }
}
object XpDropWindow : OverlayInterface() {
    override val entry = "interface.experience_drops_window"
}
    