package com.sarunas.Visma.meeting.application.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sarunas.Visma.meeting.application.model.Meeting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileService {

    @Value("${meeting.file.path}")
    private String meetingFilePath;


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveMeeting(List<Meeting> meetings) throws IOException {
        objectMapper().writeValue(new File(meetingFilePath), meetings);
    }

    public List<Meeting> getMeetings() throws IOException {
        if (Files.exists(Paths.get(meetingFilePath))) {
            byte[] jsonData = Files.readAllBytes(Paths.get(meetingFilePath));
            if (jsonData.length > 0) {
                return objectMapper().readValue(jsonData, new TypeReference<List<Meeting>>() {
                });
            }
        }
        return new ArrayList<>();
    }
}

