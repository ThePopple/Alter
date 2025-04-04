package org.alter.game.model.interf

import io.github.oshai.kotlinlogging.KotlinLogging
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import org.alter.game.type.interfacedsl.*
import org.alter.rscm.RSCM.getRSCM

class InterfaceSet {
    private val visible = Int2IntOpenHashMap()
    private var currentModal: String = "" // @TODO
    var displayMode = DisplayMode.FIXED
    fun open(destination: InterfaceDestination, Interface: Interface) {
        val child = getChildId(destination, displayMode)
        open(destination.interfaceEntry, child, Interface)
    }
    fun open(
        parent: String,
        child: String,
        Interface: Interface,
    ) {
        val hash = (getRSCM(parent) shl 16) or getRSCM(child)
        if (visible.contains(hash)) {
            closeByHash(hash)
        }
        visible[hash] = getRSCM(Interface.entry)
        // Excecutes onOpenInterface Event
    }
    fun open(
        parent: String,
        child: String,
        InterfaceEntry: String,
    ) {
        val hash = (getRSCM(parent) shl 16) or getRSCM(child)
        if (visible.contains(hash)) {
            closeByHash(hash)
        }
        visible[hash] = getRSCM(InterfaceEntry)
        // Excecutes onOpenInterface Event
    }
    fun close(Interface: Interface): Int {
        val found = visible.filterValues { it == getRSCM(Interface.entry) }.keys.firstOrNull()
        if (found != null) {
            closeByHash(found)
            return found
        }
        logger.warn {  "Interface ${getRSCM(Interface.entry)} is not visible and cannot be closed." }
        return -1
    }

    fun close(
        parent: String,
        child: String
    ): Int = closeByHash(getRSCM(parent) shl 16 or getRSCM(child))


    private fun closeByHash(hash: Int): Int {
        val found = visible.remove(hash)
        if (found != visible.defaultReturnValue()) {
            // Execute onCloseInterface event
            return found
        }
        logger.warn { "${"No interface visible in pane ({}, {})."} ${hash shr 16} ${hash and 0xFFFF}" }
        return -1
    }
    fun openModal(
        parent: String,
        child: String,
        Interface: Interface,
    ) {
        open(parent, child, Interface)
        currentModal = Interface.entry
    }
    fun openModal(
        parent: String,
        child: String,
        InterfaceEntry: String,
    ) {
        open(parent, child, InterfaceEntry)
        currentModal = InterfaceEntry
    }

    fun getModal(): String = currentModal

    fun setModal(currentModal: String) {
        this.currentModal = currentModal // @TODO Check
    }

   //fun isOccupied(
   //    destination: InterfaceDestination,
   //): Boolean = visible.containsKey((getRSCM(destination.interfaceEntry) shl 16) or getChildId(destination, displayMode))

    /**
     * @TODO Test if values represent hash or InterfaceID or Child.
     */
    fun isVisible(interfaceId: Int): Boolean {
        return visible.values.contains(interfaceId)
    }
    fun setVisible(
        parent: String,
        child: String,
        visible: Boolean,
    ) {
        val rscmParent = getRSCM(parent)
        val rscmChild = getRSCM(child)
        val hash = (rscmParent shl 16) or rscmChild
        if (visible) {
            this.visible[hash] = rscmParent
        } else {
            this.visible.remove(hash)
        }
    }
    fun setVisible(
        parent: String,
        child: Int,
        visible: Boolean,
    ) {
        val rscmParent = getRSCM(parent)
        val hash = (rscmParent shl 16) or child
        if (visible) {
            this.visible[hash] = rscmParent
        } else {
            this.visible.remove(hash)
        }
    }

    //fun getInterfaceAt(destination: InterfaceDestination) : Int = visible.getOrDefault((getRSCM(destination.interfaceEntry) shl 16) or getChildId(destination, displayMode), -1)

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
