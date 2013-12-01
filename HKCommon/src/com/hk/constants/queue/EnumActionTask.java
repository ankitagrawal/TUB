package com.hk.constants.queue;

import com.hk.domain.queue.ActionTask;

import java.util.ArrayList;
import java.util.List;

/*
 * User: Pratham
 * Date: 29/04/13  Time: 14:58
*/
public enum EnumActionTask {
    Payment_Confirmation(10L, "Payment_Confirmation",1L, 100L, 1L),
    Online_Authorization(20L, "Online_Authorization",1L, 100L, 1L),
    Create_Shipment(30L, "Create_Shipment",1L, 100L, 1L),
    Payment_Amount_Validation(50L, "Payment Amount Validated",1L, 100L, 1L),
    Process_DropShip(100L, "Process DropShip",1L, 100L, 1L),
    Create_PO(110L, "Create_PO",1L, 100L, 1L),
    AD_HOC(210L, "AD_HOC",1L, 100L, 1L),
    WH_Processing(310L, "WH_Processing",1L, 100L, 1L),
    Insufficient_Inventory(700L, "Insufficient Inventory ",1L, 100L, 1L);

    private Long id;
    private String name;
    private Long minValue;
    private Long maxValue;
    private Long priority;

    private EnumActionTask(Long id, String name , Long minValue, Long maxValue, Long priority) {
        this.id = id;
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.priority = priority;
    }

    public ActionTask asActionTask() {
        ActionTask actionTask = new ActionTask();
        actionTask.setId(id);
        actionTask.setName(name);
        actionTask.setPriority(priority);
//     this logic need to decide to get range       
        actionTask.setRange(maxValue);
        return actionTask;
    }

    public static List<Long> getActionTaskIDs(List<EnumActionTask> enumActionTasks) {
        List<Long> actionTaskIds = new ArrayList<Long>();
        for (EnumActionTask enumActionTask : enumActionTasks) {
            actionTaskIds.add(enumActionTask.getId());
        }
        return actionTaskIds;
    }

    public static ActionTask getActionTaskById(Long actionTaskId){
      for(EnumActionTask enumActionTask : values()){
         if(enumActionTask.getId().equals(actionTaskId)){
             return enumActionTask.asActionTask();
         }
      }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getMinValue() {
        return minValue;
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }
}
