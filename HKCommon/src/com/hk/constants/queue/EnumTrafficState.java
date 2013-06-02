package com.hk.constants.queue;

import com.hk.domain.queue.TrafficState;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 20:06
*/
public enum EnumTrafficState {
    NORMAL(10L, "Chill"),
    YELLOW(20L, "Mild"),
    RED(30L, "Red");

    private Long id;
    private String trafficState;

    EnumTrafficState(long id, String trafficState) {
        this.id = id;
        this.trafficState = trafficState;
    }

    public TrafficState asTrafficState(){
        TrafficState traffic = new TrafficState();
        traffic.setId(id);
        traffic.setName(trafficState);
        return traffic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrafficState() {
        return trafficState;
    }

    public void setTrafficState(String trafficState) {
        this.trafficState = trafficState;
    }
}
