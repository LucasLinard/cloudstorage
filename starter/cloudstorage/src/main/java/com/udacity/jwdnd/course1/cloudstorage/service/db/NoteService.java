package com.udacity.jwdnd.course1.cloudstorage.service.db;

import com.udacity.jwdnd.course1.cloudstorage.mapper.db.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.db.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.form.NoteForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
  private final NoteMapper noteMapper;

  public NoteService(NoteMapper noteMapper) {
    this.noteMapper = noteMapper;
  }

  public List<Note> getNotesList(Integer userId) {
    return this.noteMapper.getAllNotes(userId);
  }

  public Integer addNote(NoteForm noteform, Integer userId) {
    Note note = new Note();
    note.setNoteTitle(noteform.getNoteTitle());
    note.setNoteDescription(noteform.getNoteDescription());
    note.setUserId(userId);
    return this.noteMapper.insert(note);
  }

  public Integer deleteNote(String noteTitle, Integer userid) {
    return this.noteMapper.delete(noteTitle, userid);
  }

  public Note getNote(String noteTitle, Integer userId) {
    return this.noteMapper.getNote(noteTitle, userId);
  }

  public void updateNote(NoteForm noteForm) {
    this.noteMapper.updateNote(
        noteForm.getNoteTitle(), noteForm.getNoteDescription(), noteForm.getNoteId());
  }
}
