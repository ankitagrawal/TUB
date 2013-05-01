package com.hk.constants.queue;

import com.hk.domain.queue.ActionTask;

import java.util.ArrayList;
import java.util.List;

/*
 * User: Pratham
 * Date: 29/04/13  Time: 14:58
*/
public enum EnumActionTask {
    Payment_Confirmation(10L, "Payment_Confirmation"),
    Online_Authorization(20L, "Online_Authorization"),
    Create_Shipment(30L, "Create_Shipment"),
    Create_PO(110L, "Create_PO");

    private Long id;
    private String name;

    private EnumActionTask(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ActionTask asActionTask() {
        ActionTask actionTask = new ActionTask();
        actionTask.setId(id);
        actionTask.setName(name);
        return actionTask;
    }

    public static List<Long> getActionTaskIDs(List<EnumActionTask> enumActionTasks) {
        List<Long> actionTaskIds = new ArrayList<Long>();
        for (EnumActionTask enumActionTask : enumActionTasks) {
            actionTaskIds.add(enumActionTask.getId());
        }
        return actionTaskIds;
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
}
