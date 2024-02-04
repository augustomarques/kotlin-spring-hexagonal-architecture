package br.com.amarques.person.repository

import br.com.amarques.person.entity.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<PersonEntity, Long> {

    fun findByName(name: String): PersonEntity?
}
