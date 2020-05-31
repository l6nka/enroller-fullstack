package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {

        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") long meetingId) {
        Meeting meeting = meetingService.findById(meetingId);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity("Meeting with Id " + meeting.getId() + " already exists", HttpStatus.CONFLICT);
        }
        meetingService.add(meeting);
        return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long meetingId,
                                                     @RequestBody Participant participant) {
        Meeting foundMeeting = meetingService.findById(meetingId);
        if (foundMeeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.addParticipantToMeeting(foundMeeting, participant);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("id") long meetingId,
                                                          @PathVariable("login") String login) {
        Meeting foundMeeting = meetingService.findById(meetingId);
        Participant foundParticipant = participantService.findByLogin(login);
        meetingService.removeParticipantFromMeeting(foundMeeting, foundParticipant);
        meetingService.removeParticipantFromMeeting(foundMeeting, foundParticipant);
        return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipantsFromMeeting(@PathVariable("id") long meetingId) {
        Meeting foundMeeting = meetingService.findById(meetingId);
        if (foundMeeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Participant>>(foundMeeting.getParticipants(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long meetingId) {
        Meeting foundMeeting = meetingService.findById(meetingId);
        if (foundMeeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.delete(foundMeeting);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") long meetingId,
                                           @RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meetingId);
        if (foundMeeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        meetingService.update(meeting);
        return new ResponseEntity<Meeting>(meetingService.findById(meetingId), HttpStatus.OK);
    }
}
