package com.siddhant.boxly.repositories;

import com.siddhant.boxly.entities.File;
import com.siddhant.boxly.entities.User;
import com.siddhant.boxly.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

    List<File> findByCreatedByAndStatus(User user, Status status);
    List<File> findBySharedWithAndStatus(User user,Status status);
}
