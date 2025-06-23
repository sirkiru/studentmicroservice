package com.myschool.studentmicroservice.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myschool.studentmicroservice.model.Student;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
}