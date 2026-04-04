package com.pracsession.myFirstProject.service;

import com.pracsession.myFirstProject.entity.User;
import com.pracsession.myFirstProject.repository.JournalEntryRepository;
import com.pracsession.myFirstProject.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userservice;

    Logger logger = org.slf4j.LoggerFactory.getLogger(JournalEntryService.class);
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username){
        try {
            User user = userservice.getUserByUsername(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userservice.saveUser(user);
        }
        catch (Exception e){
            logger.error("Error saving journal entry for user: {}", username, e);
             throw e;
        }
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntry.setDate(LocalDateTime.now());
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllEntries(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public boolean deleteById(ObjectId id, String username){
        boolean removed = false;
        try {
            User user = userservice.getUserByUsername(username);
            removed = user.getJournalEntries().removeIf((x -> x.getId().equals(id)));
            if (removed) {
                userservice.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        }
        catch (Exception e){
            logger.error("Error deleting journal entry with id: {} for user: {}", id, username, e);
            throw e;
        }
        return removed;

    }

}
