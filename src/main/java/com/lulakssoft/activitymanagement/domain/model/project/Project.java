package com.lulakssoft.activitymanagement.domain.model.project;

import java.util.*;

public class Project {
    private final String id;
    private String name;
    private final String creatorId;
    private Set<String> memberIds;

    public Project(String name, String creatorId, Set<String> memberIds) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.creatorId = creatorId;
        this.memberIds = memberIds;
        this.memberIds.add(creatorId); // Creator is automatically a member
    }

    // Konstruktor für Repository
    public Project(String id, String name, String creatorId, Set<String> memberIds) {
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
        this.memberIds = memberIds;
    }

    public void addMember(String userId) {
        memberIds.add(userId);
    }

    public void removeMember(String userId) {
        if (!userId.equals(creatorId)) { // Prevent removing creator
            memberIds.remove(userId);
        }
    }

    public boolean isMember(String userId) {
        return memberIds.contains(userId);
    }

    // Getter
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCreatorId() { return creatorId; }
    public Set<String> getMemberIds() { return Collections.unmodifiableSet(memberIds); }

    // Setter
    public void setName(String name) { this.name = name; }
    public void setMemberIds(Set<String> memberIds) {
        this.memberIds = memberIds;
    }
    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", memberIds=" + memberIds +
                '}';
    }
}