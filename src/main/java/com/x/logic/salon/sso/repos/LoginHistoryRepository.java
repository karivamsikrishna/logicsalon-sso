package com.x.logic.salon.sso.repos;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.x.logic.salon.sso.modal.LoginHistory;


@Repository
public interface LoginHistoryRepository extends MongoRepository<LoginHistory, String> {
}
