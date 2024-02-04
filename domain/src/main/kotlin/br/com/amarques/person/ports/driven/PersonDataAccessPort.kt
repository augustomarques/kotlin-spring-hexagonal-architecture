package br.com.amarques.person.ports.driven

import br.com.amarques.person.model.Person
import br.com.amarques.person.model.PersonToSave

interface PersonDataAccessPort {

    suspend fun findById(id: Long): Person?

    suspend fun findByName(name: String): Person?

    suspend fun getAll(): List<Person>

    suspend fun save(personToSave: PersonToSave): Person

    suspend fun update(id: Long, personToSave: PersonToSave): Person

    suspend fun delete(id: Long)
}
