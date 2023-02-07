package compose.notezz.model

sealed class ConnectionState{
    object Available : ConnectionState()
    object UnAvailable: ConnectionState()

}
