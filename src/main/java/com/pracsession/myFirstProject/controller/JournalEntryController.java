package com.pracsession.myFirstProject.controller;

import com.pracsession.myFirstProject.entity.JournalEntry;
import com.pracsession.myFirstProject.entity.User;
import com.pracsession.myFirstProject.service.JournalEntryService;
import com.pracsession.myFirstProject.service.UserService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private static final Logger log = LoggerFactory.getLogger(JournalEntryController.class);
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<JournalEntry>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        log.info("Getting all the journal entries of user: {}", username);
        List<JournalEntry> result = user.getJournalEntries();
        log.info("Printing the result of get all entries: {}", result);
        if (result.isEmpty()) {
            log.warn("No entries found for user: {}", username);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("id/{myid}")
    public ResponseEntity<JournalEntry> findById(@PathVariable ObjectId myid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<JournalEntry> journalEntry = user.getJournalEntries().stream().filter(entry -> entry.getId().equals(myid)).toList();
        if (!journalEntry.isEmpty()) {
            Optional<JournalEntry> result = journalEntryService.getById(myid);
            if (result.isPresent()) {
                log.info("Entry found with id: {}", myid);
                return new ResponseEntity<>(result.get(), HttpStatus.OK);
            }
        }
        log.warn("No entry found with id: {}", myid);
        return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("id/{myid}")
    public ResponseEntity<?> removeEntry(@PathVariable ObjectId myid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = journalEntryService.deleteById(myid, username);
        if (removed) {
            log.info("Entry with id: {} removed successfully for user: {}", myid, username);
            return new ResponseEntity<JournalEntry>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Entry with id: " + myid + " not found for user: " + username, HttpStatus.NOT_FOUND);

    }

    @PutMapping("id/{myid}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId myid, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid)).toList();
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getById(myid);
            if (journalEntry.isEmpty()) {
                log.warn("No entry found with id: {} for user: {}", myid, username);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                log.info("Entry found with id: {} for user: {}, updating entry", myid, username);
                JournalEntry old = journalEntry.get();
                if (!old.getTitle().equals(newEntry.getTitle())) {
                    log.info("Updating title of entry with id: {} for user: {}", myid, username);
                    old.setTitle(newEntry.getTitle());
                }
                if (!old.getContent().equals(newEntry.getContent())) {
                    log.info("Updating content of entry with id: {} for user: {}", myid, username);
                    old.setContent(newEntry.getContent());
                }
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
