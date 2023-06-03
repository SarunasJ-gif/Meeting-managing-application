package com.sarunas.Visma.meeting.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {MeetingNotFoundException.class})
    public ResponseEntity<Object> handleMeetingNotFoundException(MeetingNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                e.getMessage(),
                HttpStatus.NO_CONTENT,
                LocalDateTime.now());
        return new ResponseEntity<>(errorMessage, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(value = {PersonNotFoundException.class})
    public ResponseEntity<Object> handlePersonNotFoundException(PersonNotFoundException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                e.getMessage(),
                HttpStatus.NO_CONTENT,
                LocalDateTime.now());
        return new ResponseEntity<>(errorMessage, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(value = {RightToDeleteException.class})
    public ResponseEntity<Object> handleRightToDeleteException(RightToDeleteException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                e.getMessage(),
                HttpStatus.FORBIDDEN,
                LocalDateTime.now());
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {PersonExistsInMeetingException.class})
    public ResponseEntity<Object> handlePersonExistsInMeetingException(PersonExistsInMeetingException e) {
        ErrorMessage errorMessage = new ErrorMessage(
                e.getMessage(),
                HttpStatus.FORBIDDEN,
                LocalDateTime.now());
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

}
