package com.example.rabbitmqdemo.message;

import java.time.LocalDateTime;

public class CourseNoticeMessage {

    private String requestId;
    private Long studentId;
    private Long teachingClassId;
    private String content;
    private LocalDateTime createdAt;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTeachingClassId() {
        return teachingClassId;
    }

    public void setTeachingClassId(Long teachingClassId) {
        this.teachingClassId = teachingClassId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CourseNoticeMessage{" +
            "requestId='" + requestId + '\'' +
            ", studentId=" + studentId +
            ", teachingClassId=" + teachingClassId +
            ", content='" + content + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
