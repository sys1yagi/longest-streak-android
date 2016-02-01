package com.sys1yagi.longeststreakandroid.tool

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.sys1yagi.longeststreakandroid.model.Event
import java.lang.reflect.Type

object GsonProvider {
    val instance: Gson = GsonBuilder()
            .registerTypeAdapter(EventTypeConverter.TYPE, EventTypeConverter())
            .create()

    class EventTypeConverter : JsonSerializer<Event.Type>, JsonDeserializer<Event.Type> {

        override fun serialize(src: Event.Type, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src.toString())
        }

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Event.Type {
            return Event.Type.of(json.asString)
        }

        companion object {
            val TYPE = object : TypeToken<Event.Type>() {
            }.type
        }
    }

}
