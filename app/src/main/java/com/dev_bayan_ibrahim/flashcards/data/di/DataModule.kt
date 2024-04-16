package com.dev_bayan_ibrahim.flashcards.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.dev_bayan_ibrahim.flashcards.data.data_source.datastore.DataStoreManager
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.db.database.FlashDatabase
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.storage.FlashFileManager
import com.dev_bayan_ibrahim.flashcards.data.data_source.local.storage.FlashFileManagerImpl
import com.dev_bayan_ibrahim.flashcards.data.data_source.remote.uri_decerator.UriDirector
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepo
import com.dev_bayan_ibrahim.flashcards.data.repo.FlashRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLBuilder
import io.ktor.http.set
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ): FlashDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = FlashDatabase::class.java,
            name = "db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext
        context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        produceFile = {
            context.filesDir.resolve(
                "datastore.preferences_pb"
            ).absolutePath.toPath()
        }
    )
    @Provides
    @Singleton
    fun provideDataStoreManager(
        dataStore: DataStore<Preferences>,
    ): DataStoreManager = DataStoreManager(dataStore)

    @Provides
    @Singleton
    fun provideRepository(
        db: FlashDatabase,
        dataStore: DataStore<Preferences>,
        fileManager: FlashFileManager,
        json: Json,
        director: UriDirector,
        client: HttpClient,
    ): FlashRepo = FlashRepoImpl(
        db = db,
        preferences = DataStoreManager(dataStore),
        fileManager = fileManager,
        json = json,
        director = director,
        client = client
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        prettyPrint = true
    }
    @Provides
    @Singleton
    fun providesHttpClient(
        json: Json,
    ): HttpClient = HttpClient {
        expectSuccess = true
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(json = json)
        }
    }

    @Provides
    @Singleton
    fun provideFileManager(
        client: HttpClient,
        @ApplicationContext
        context: Context,
        coroutinesContext: CoroutineContext,
    ): FlashFileManager = FlashFileManagerImpl(
        filesDir = context.filesDir,
        client = client,
        context = coroutinesContext,
    )

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineContext = Dispatchers.IO

    @Provides
    @Singleton
    fun provideSwatUriDirector(
    ): UriDirector = object : UriDirector {
        override fun URLBuilder.director(): URLBuilder {
            set(
                scheme = "https",
                // don't hard code this host, chnage the vairable instead, it is used in other places
                host = apiHost,
                path = null
            )
            return this
        }
    }

    @Provides
    @Singleton
    fun providesAppInstallationIdMonitor(): AppInstallation {
        return FirebaseAppInstallation()
    }
}
private const val apiHost = "one18-team.onrender.com"