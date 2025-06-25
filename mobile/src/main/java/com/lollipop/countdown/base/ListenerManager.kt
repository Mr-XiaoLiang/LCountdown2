package com.lollipop.countdown.base

interface ListenerDispatcher<T> {
    fun register(listener: T)
    fun unregister(listener: T)
}

class ListenerManager<T : Any>: ListenerDispatcher<T> {

    private val listeners = mutableListOf<T>()

    override fun register(listener: T) {
        listeners.add(listener)
    }

    override fun unregister(listener: T) {
        listeners.remove(listener)
    }

    fun notify(action: (T) -> Unit) {
        listeners.forEach {
            action(it)
        }
    }

    fun clear() {
        listeners.clear()
    }

}