package com.amazonaws.services.msf.event;

public enum EventType {
    IDLE,               // A-1 공회전
    ACC_DECEL,          // B-1 급가/감속
    OVERSPEED           // B-2 과속
    // 추후 SHARP_TURN, LANE_DEPART 등 추가
}