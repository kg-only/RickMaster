package com.example.rickmaster.di

import com.example.rickmaster.data.repository.CameraRepositoryImpl
import com.example.rickmaster.data.repository.DoorsRepositoryImpl
import com.example.rickmaster.domain.repository.CameraRepository
import com.example.rickmaster.domain.repository.DoorsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.realm.Realm
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCameraRepository(httpClient: HttpClient, realm:Realm): CameraRepository {
        return CameraRepositoryImpl(httpClient,realm)
    }

    @Provides
    @Singleton
    fun provideDoorsRepository(httpClient: HttpClient , realm: Realm): DoorsRepository {
        return DoorsRepositoryImpl(httpClient,realm)
    }

    @Singleton
    @Provides
    fun provideRealm(): Realm = Realm.getDefaultInstance()
}