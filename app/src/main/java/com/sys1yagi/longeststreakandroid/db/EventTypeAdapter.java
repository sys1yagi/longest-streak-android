package com.sys1yagi.longeststreakandroid.db;


import com.github.gfx.android.orma.annotation.StaticTypeAdapter;
import com.sys1yagi.longeststreakandroid.model.Event;

@StaticTypeAdapter(targetType = Event.Type.class, serializedType = String.class)
public class EventTypeAdapter {

    public static String serialize(Event.Type source) {
        return source.name();
    }

    public static Event.Type deserialize(String serialized) {
        return Event.Type.of(serialized);
    }
}

