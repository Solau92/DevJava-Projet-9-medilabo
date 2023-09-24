package com.medilabo.medilabonoteapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.medilabo.medilabonoteapp.entity.Note;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

	public List<Note> findByPatientId(int id);
}
