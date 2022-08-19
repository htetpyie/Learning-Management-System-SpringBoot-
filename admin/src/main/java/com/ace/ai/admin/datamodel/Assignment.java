package com.ace.ai.admin.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Assignment implements Serializable {
    
    @Id
    private int id;
    private String name;
    private String filePath;
    private String assignmentChapterName;
    private String branch;

    @OneToMany(mappedBy = "assignment")
    private List<StudentAssignmentMark> studentAssignmentMarks = new ArrayList<>();
}