//package org.alter.plugins.content.interfaces.gameframe.xpdrops
//
//import org.alter.api.*
//import org.alter.api.cfg.*
//import org.alter.api.dsl.*
//import org.alter.api.ext.*
//import org.alter.game.*
//import org.alter.game.model.*
//import org.alter.game.model.attr.*
//import org.alter.game.model.container.*
//import org.alter.game.model.container.key.*
//import org.alter.game.model.entity.*
//import org.alter.game.model.item.*
//import org.alter.game.model.queue.*
//import org.alter.game.model.shop.*
//import org.alter.game.model.timer.*
//import org.alter.game.plugin.*
//import org.alter.game.type.interfacedsl.Interface
//import org.alter.game.type.interfacedsl.OverlayInterface
//import org.alter.game.type.interfacedsl.overlayInterface
//import org.alter.plugins.content.interfaces.xpdrops.XpSettings
//
//class XpDropsPlugin(
//    r: PluginRepository,
//    world: World,
//    server: Server
//) : KotlinPlugin(r, world, server) {
//
//    init {
//        val INTERFACE_ID = 122
//
//        onButton(interfaceId = 160, component = 5) {
//            val option = player.getInteractingOption()
//            player.playSound(Sound.INTERFACE_SELECT1)
//
//            when (option) {
//
//                2 -> {
//                    if (player.lock.canInterfaceInteract()) {
//                        XpSettings.open(player)
//                    }
//                }
//            }
//        }
//
//
//        Interface(XpDropInterface) {
//            component()
//        }
//    }
//}
//
//object XpDropInterface: OverlayInterface() {
//    override val entry = "interface.experience_drops_window"
//}
