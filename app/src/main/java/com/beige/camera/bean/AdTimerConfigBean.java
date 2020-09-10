package com.beige.camera.bean;

import com.google.gson.annotations.SerializedName;

public class AdTimerConfigBean {

    /**
     * 下一次的钱，为0时，循环次数用尽
     */
    @SerializedName("next_reward_value")
    private int nextRewardValue;
    @SerializedName("task_id")
    private String taskId;
    private int second;

    public int getNextRewardValue() {
        return nextRewardValue;
    }

    public void setNextRewardValue(int nextRewardValue) {
        this.nextRewardValue = nextRewardValue;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
