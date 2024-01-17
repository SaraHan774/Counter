package com.august.counter

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserLocalDateSerializer: Serializer<UserLocalDate> {
    override val defaultValue: UserLocalDate
        get() = UserLocalDate.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserLocalDate {
        try {
            return UserLocalDate.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: UserLocalDate, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.userLocalDateStore: DataStore<UserLocalDate> by dataStore(
    fileName = "ilysb.pb",
    serializer = UserLocalDateSerializer
)