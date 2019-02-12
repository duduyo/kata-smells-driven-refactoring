package com.orange.callhistory.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CallRepository : CrudRepository<CallDao, String>
