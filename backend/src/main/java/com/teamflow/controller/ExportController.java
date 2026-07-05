package com.teamflow.controller;

import com.teamflow.entity.Project;
import com.teamflow.entity.RootCauseAnalysis;
import com.teamflow.entity.Task;
import com.teamflow.repository.ProjectRepository;
import com.teamflow.repository.RootCauseAnalysisRepository;
import com.teamflow.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exports")
@RequiredArgsConstructor
public class ExportController {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final RootCauseAnalysisRepository rootCauseAnalysisRepository;

    @GetMapping("/tasks.csv")
    public ResponseEntity<String> tasksCsv() {
        StringBuilder csv = new StringBuilder("id,title,status,priority,dueDate,projectId,assigneeId\n");
        for (Task task : taskRepository.findAll()) {
            csv.append(task.getId()).append(',')
                    .append(escape(task.getTitle())).append(',')
                    .append(task.getStatus()).append(',')
                    .append(task.getPriority()).append(',')
                    .append(value(task.getDueDate())).append(',')
                    .append(task.getProject().getId()).append(',')
                    .append(task.getAssignee() == null ? "" : task.getAssignee().getId())
                    .append('\n');
        }
        return csv("teamflow-tasks.csv", csv.toString());
    }

    @GetMapping("/projects.csv")
    public ResponseEntity<String> projectsCsv() {
        StringBuilder csv = new StringBuilder("id,name,description,createdById\n");
        for (Project project : projectRepository.findAll()) {
            csv.append(project.getId()).append(',')
                    .append(escape(project.getName())).append(',')
                    .append(escape(project.getDescription())).append(',')
                    .append(project.getCreatedBy().getId())
                    .append('\n');
        }
        return csv("teamflow-projects.csv", csv.toString());
    }

    @GetMapping("/root-cause-analyses.csv")
    public ResponseEntity<String> rcaCsv() {
        StringBuilder csv = new StringBuilder("id,taskId,status,problemSummary,rootCause,correctiveAction,preventiveAction\n");
        for (RootCauseAnalysis analysis : rootCauseAnalysisRepository.findAll()) {
            csv.append(analysis.getId()).append(',')
                    .append(analysis.getTask().getId()).append(',')
                    .append(analysis.getStatus()).append(',')
                    .append(escape(analysis.getProblemSummary())).append(',')
                    .append(escape(analysis.getRootCause())).append(',')
                    .append(escape(analysis.getCorrectiveAction())).append(',')
                    .append(escape(analysis.getPreventiveAction()))
                    .append('\n');
        }
        return csv("teamflow-rca.csv", csv.toString());
    }

    private ResponseEntity<String> csv(String fileName, String body) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(body);
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private String value(Object value) {
        return value == null ? "" : value.toString();
    }
}

