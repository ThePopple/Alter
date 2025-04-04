@file:Suppress("unused")
package org.alter.game.type.interfacedsl

interface Interface {
    val entry: String
    val type: InterfaceType
    //val hash: Int
}
abstract class ModalInterface : Interface {
    override val type: InterfaceType = InterfaceType.ModalInterface
}
abstract class OverlayInterface : Interface {
    override val type: InterfaceType = InterfaceType.OverlayInterface
}