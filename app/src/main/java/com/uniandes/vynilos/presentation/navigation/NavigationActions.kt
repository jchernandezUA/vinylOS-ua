package com.uniandes.vynilos.presentation.navigation

class NavigationActions (
    val onAction: (type: ActionType) -> Unit = {}
)

enum class ActionType {
    CLICK_NOT_MAIN
}