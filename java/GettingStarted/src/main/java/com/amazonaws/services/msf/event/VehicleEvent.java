package com.amazonaws.services.msf.event;

import com.fasterxml.jackson.databind.ObjectMapper;

public class VehicleEvent {
    public EventType type;
    public long timestamp;
    public String raw;

    public VehicleEvent() {}

    public VehicleEvent(EventType type, long ts, String raw) {
        this.type = type;
        this.timestamp = ts;
        this.raw = raw;
    }

    // 직렬화 편의 메서드
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Override public String toString() {
        try { return MAPPER.writeValueAsString(this); }
        catch (Exception e) { return "{\"error\":\"serialize\"}"; }
    }
}
