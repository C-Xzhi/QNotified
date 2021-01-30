package me.singleneuron.qn_kernel.ui.base

sealed class QNPreference {
    abstract val name:String
}

sealed class QNPreferenceItem(override val name: String, open val description: String, open val onClick: ()->Unit) : QNPreference() {
    constructor(name: String, description: String) : this(name, description, {})
}

data class QNPreferenceSwitch(override val name: String, override val description: String, val onSwitchChanged: (Boolean)->Unit) : QNPreferenceItem(name,description)

data class QNPreferenceScreen(override val name: String, override val description: String, val items: List<QNPreference>) : QNPreferenceItem(name,description)

data class QNPreferenceCategory(override val name: String, val items: List<QNPreferenceItem>) : QNPreference()
