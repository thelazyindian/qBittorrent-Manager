package dev.yashgarg.qbit

import dev.yashgarg.qbit.data.manager.ClientManager
import dev.yashgarg.qbit.data.models.ConfigStatus
import dev.yashgarg.qbit.data.models.ConnectionType
import dev.yashgarg.qbit.data.models.ServerConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import qbittorrent.QBittorrentClient

class FakeClientManager : ClientManager {
    private val baseUrl: String by lazy { System.getenv("base_url") }
    private val password: String by lazy { System.getenv("password") }

    val config =
        ServerConfig(
            0,
            "TestServer",
            baseUrl,
            443,
            "admin",
            password,
            ConnectionType.HTTPS,
        )

    private val _configStatus = MutableSharedFlow<ConfigStatus>()
    override val configStatus: SharedFlow<ConfigStatus>
        get() = _configStatus.asSharedFlow()

    override suspend fun checkAndGetClient() =
        QBittorrentClient(
            "${config.connectionType}://${config.baseUrl}",
            config.username,
            config.password,
            syncInterval = ClientManager.syncInterval,
            httpClient = ClientManager.httpClient,
            dispatcher = Dispatchers.Default,
        )
}
