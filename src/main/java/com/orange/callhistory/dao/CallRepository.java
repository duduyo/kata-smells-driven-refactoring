package com.orange.callhistory.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallRepository extends CrudRepository<CallDao, String> {

}
