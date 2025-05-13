package com.amazonaws.services.msf.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class Telemetry {
    public double velocity;         // km/h 기준
    @JsonProperty("driveId") public double driveId;
    @JsonProperty("Accelero_x") public double accelX;
    @JsonProperty("Accelero_y") public double accelY;
    @JsonProperty("Accelero_z") public double accelZ;
    @JsonProperty("Gyroscop_x") public double gyroX;
    @JsonProperty("Gyroscop_y") public double gyroY;
    @JsonProperty("Gyroscop_z") public double gyroZ;
    @JsonProperty("GNSS_x") public double gnssX;
    @JsonProperty("GNSS_y") public double gnssY;

    public double Throttle;
    public double Steer;            // -1.0 ~ 1.0 범위 → 각도(°) 변환은 EventDetector에서
    public double Brake;

    public JsonNode collision;        // JSON에 객체 → Jackson이 String으로 매핑
    public JsonNode invasion;
    @JsonProperty("front_distance") public double frontDistance;
    @JsonProperty("front_object")  public String frontObject;

    // Jackson용 기본 생성자·getter/setter 생략 가능(필드 public이면)
}
