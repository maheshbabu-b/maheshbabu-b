package com.mahesh.projectone.repository;

import com.mahesh.projectone.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,String> {
}
