package com.sharaga.markstudents.server.dto;

import com.sharaga.markstudents.server.entity.GroupFromSSTU;

import java.util.ArrayList;
import java.util.List;

public class ArrayGroupDTO {
    private List<GroupFromSSTU> groups = new ArrayList<GroupFromSSTU>();

    public List<GroupFromSSTU> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupFromSSTU> groups) {
        this.groups = groups;
    }
}
