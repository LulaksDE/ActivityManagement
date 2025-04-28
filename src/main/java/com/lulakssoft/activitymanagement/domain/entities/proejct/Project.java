package com.lulakssoft.activitymanagement.domain.entities.proejct;

import com.lulakssoft.activitymanagement.adapter.notification.LoggerFactory;
import com.lulakssoft.activitymanagement.adapter.notification.LoggerNotifier;
import com.lulakssoft.activitymanagement.domain.entities.activity.Activity;
import com.lulakssoft.activitymanagement.domain.repositories.IActivityRepository;
import com.lulakssoft.activitymanagement.operation.ActivityRepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {
    private String id;
    private String name;
    private final List<Activity> activityList;
    private String creatorId;
    private List<String> userIdList;
    private final IActivityRepository activityRepository;
    private final LoggerNotifier logger = LoggerFactory.getLogger();

    public Project(String name, String creatorId, List<String> userIdList, IActivityRepository activityRepository) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.activityList = new ArrayList<>();
        this.creatorId = creatorId;
        this.userIdList = userIdList != null ? userIdList : new ArrayList<>();
        this.activityRepository = activityRepository;
    }

    public void addActivity(Activity activity) {
        try{
        activity = activityRepository.save(activity);
        }catch (ActivityRepositoryException e){
            logger.logError("Error saving activity in project entity: ", e);
        }
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityRepository.delete(activity);
        activityList.remove(activity);
    }

    public void refreshActivities() {
        List<Activity> activitiesFromDb = activityRepository.findByProjectId(this.id);
        this.activityList.clear();
        this.activityList.addAll(activitiesFromDb);
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public List<String> getMembers() {
        return userIdList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creatorId;
    }

    public void setCreator(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creatorId=" + creatorId +
                " userList=" + userIdList.toString() +
                ", activityList=" + this.activityList.toString() +
                '}';
    }
}
