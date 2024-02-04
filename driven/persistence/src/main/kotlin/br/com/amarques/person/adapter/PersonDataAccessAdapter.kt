package br.com.amarques.person.adapter

import br.com.amarques.person.entity.PersonEntity
import br.com.amarques.person.entity.toEntity
import br.com.amarques.person.exception.PersonNotFoundException
import br.com.amarques.person.model.Person
import br.com.amarques.person.model.PersonToSave
import br.com.amarques.person.ports.driven.PersonDataAccessPort
import br.com.amarques.person.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonDataAccessAdapter(
    private val personRepository: PersonRepository
) : PersonDataAccessPort {

    override suspend fun findById(id: Long) =
        getOne(id).toModel()

    override suspend fun findByName(name: String): Person? =
        personRepository.findByName(name)?.toModel()

    override suspend fun getAll() =
        personRepository.findAll().map { it.toModel() }

    override suspend fun save(personToSave: PersonToSave) =
        personRepository.save(personToSave.toEntity()).toModel()

    override suspend fun update(id: Long, personToSave: PersonToSave) =
        getOne(id).let { personRepository.save(it.merge(personToSave)).toModel() }

    override suspend fun delete(id: Long) {
        getOne(id).let { personRepository.delete(it) }
    }

    private fun getOne(id: Long): PersonEntity =
        personRepository.findById(id).orElseThrow { PersonNotFoundException(id) }
}
