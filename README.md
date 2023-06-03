

# Visma Meeting Application

---

## Table of Contents

- [Description](#Description)
- [Prerequisites](#Prerequisites)
- [Configuration](#Configuration)
- [Dependencies](#Dependencies)
- [Project Structure](#Project-Structure)
- [Getting Started](#Getting-Started)
- [API Endpoints](#API-Endpoints)

---

## Description

This is a README file for a Java Spring application that provides REST API endpoints for managing meetings. 
The application allows users to create, delete meetings, add or remove attendees from meetings, and list meetings 
with various filtering options. The meeting data is stored in a JSON file, and the application retains the 
data between restarts.



---

## Prerequisites

- Java Development Kit (JDK) 8 or later
- Gradle build tool
- Any required IDE (e.g., IntelliJ IDEA, Eclipse, etc.)

---

## Configuration

- src/main/resources/application.properties: Contains application-specific configurations

---

## Dependencies

The project includes the necessary dependencies for building a Java Spring application. You can find the list of
dependencies and their versions in the build.gradle file.


---

## Project Structure

The project follows the standard Java Spring project structure:
- src/main/java: Contains the Java source code of the application.
- src/main/resources: Contains configuration files and resources.
- src/test: Contains the unit tests for the application.

---

## Getting Started

1. Clone the repository to your local machine:
-      $ git clone <repository_url>
2. Navigate to the project directory:
-      $ cd <project_directory>
3. Build the application using Gradle:
-      $ gradle build
4. There are a few ways how you can run the starter application:
    * Right click on _src/main/java/com/sarunas/Visma/meeting/application/VismaMeetingApplication.java_ and select _Run_
    * Use `gradlew bootRun` to run the starter application.
  
---

## API Endpoints

The application provides the following REST API endpoints:

1. Create a new meeting:
- Endpoint: `POST /meeting`
- Description: when creating a meeting, the responsible person is automatically added to the list of participants  
- Request Body:
```
  {
  "name": "Visma Meeting",
  "responsiblePerson": {
  "id": 1,
  "name": "John Snow",
  "email": "john@mail.com"
  },
  "description": "Visma John Snow meeting",
  "category": "Hub",
  "type": "Live",
  "startDate": "2023-06-05 12:45:00",
  "endDate": "2023-06-05 13:00:00",
  "persons": []
  }
```
  
- Response: Status: 201 CREATED
- Body:

```
  {
  "id": 1685783150264787,
  "name": "Visma Meeting",
  "responsiblePerson": {
  "id": 1,
  "name": "John Snow",
  "email": "john@mail.com"
  },
  "description": "Visma John Snow meeting",
  "category": "HUB",
  "type": "LIVE",
  "startDate": "2023-06-05 12:45:00",
  "endDate": "2023-06-05 13:00:00",
  "persons": [
  {
    "id": 1,
    "name": "John Snow",
    "email": "john@mail.com"
  }]
  }
```

2. Delete meeting:
- Description: Only the responsible person can delete the meeting.
- Endpoint: `DELETE /meeting/{meetingId}/responsiblePerson/{personId}`
- Response: Status: 200 OK

3. Add a person to the meeting:
- Endpoint: `POST /meeting/{meetingId}/addPerson`
- Request Body:

```
  {
  "id": 2,
  "name": "Sansa Stark",
  "email": "sansa@mail.com"
  }
```

- Response: Status: 200 OK
- Body:

```
   {
   "id": 2,
   "name": "Sansa Stark",
   "email": "sansa@mail.com"
   "addedTimestamp": "2023-06-05 11:11:37"
   }
```

4. Remove Person from meeting:
- The responsible person cannot be removed from the meeting.
- `DELETE /meeting/{meetingId}/personToDelete/{personId}`
- Response: Status: 200 OK


5. List all meetings with filtering options:
- Endpoint: `GET /meeting/filteredMeetings`
- Query Parameters:\
  -`description`: Filter by meeting description: `GET /meeting/filteredMeetings?description=Visma John Snow Meeting`\
  -`responsiblePerson`: Filter by responsible person:  `GET /meeting/filteredMeetings?responsiblePerson=John Snow`\
  -`category`: Filter by category: `GET /meeting/filteredMeetings?category=Hub`\
  -`type`: Filter by type: `GET /meeting/filteredMeetings?type=Live`\
  -`startDate` or `endDate`: Filter by dates: `GET /meeting/filteredMeetings?startDate=2023-06-02&endDate=2023-06-06`\
  -`numberOfAttendees`: Filter by number of attendees (e.g. show meetings that have over 10 people attending):
  `GET /meeting/filteredMeetings?numberOfAttendees=10`

---