package com.lefrantguillaume.components.taskComponent;

import com.lefrantguillaume.utils.stockage.Tuple;

/**
 * Created by andres_k on 30/05/2015.
 */
public class TaskFactory {
    public static Tuple<EnumTargetTask, EnumTargetTask, Object> createTask(EnumTargetTask sender, Tuple<EnumTargetTask, EnumTargetTask, Object> task){
        Tuple<EnumTargetTask, EnumTargetTask, Object> result = new Tuple<>(sender, task.getV2(), task.getV3());
        return result;
    }

    public static Tuple<EnumTargetTask, EnumTargetTask, Object> createTask(EnumTargetTask sender, EnumTargetTask target, Object task){
        Tuple<EnumTargetTask, EnumTargetTask, Object> result = new Tuple<>(sender, target, task);
        return result;
    }
}
