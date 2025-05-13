package com.amazonaws.services.msf.processor;

import com.amazonaws.services.msf.event.EventType;
import com.amazonaws.services.msf.event.VehicleEvent;
import com.amazonaws.services.msf.model.Telemetry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;

public class EventDetector extends RichFlatMapFunction<String, String> {

    private transient ObjectMapper mapper;
    private transient ValueState<Double> lastVelocity;      // B-1용
    private transient ValueState<Long> zeroSince;           // A-1용 (velocity==0 시작 시각)

    @Override
    public void open(org.apache.flink.configuration.Configuration parameters) {
        mapper = new ObjectMapper();
        lastVelocity = getRuntimeContext().getState(
                new ValueStateDescriptor<>("lastVelocity", Double.class));
        zeroSince = getRuntimeContext().getState(
                new ValueStateDescriptor<>("zeroSince", Long.class));
    }

    @Override
    public void flatMap(String json, Collector<String> out) throws Exception {
        Telemetry t = mapper.readValue(json, Telemetry.class);
        long now = System.currentTimeMillis();

        // ---------- A-1 공회전 ----------
        if (t.velocity == 0) {
            if (zeroSince.value() == null) {
                zeroSince.update(now);
            } else if (now - zeroSince.value() >= 1_000) {        // 1초
                out.collect(new VehicleEvent(EventType.IDLE, now, json).toString());
                zeroSince.update(now);      // 다시 0부터 카운트
            }
        } else {
            zeroSince.clear();
        }

        // ---------- B-1 급가/감속 ----------
        Double prevVel = lastVelocity.value();
        if (prevVel != null && Math.abs(t.velocity - prevVel) >= 10) {
            out.collect(new VehicleEvent(EventType.ACC_DECEL, now, json).toString());
        }
        lastVelocity.update(t.velocity);

        // ---------- B-2 과속 ----------
        if (t.velocity >= 30) {
            out.collect(new VehicleEvent(EventType.OVERSPEED, now, json).toString());
        }
    }
}
