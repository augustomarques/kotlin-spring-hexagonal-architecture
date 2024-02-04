package br.com.amarques.person.ports.driver

import br.com.amarques.person.model.Person
import br.com.amarques.person.model.PersonToSave

interface CreatePersonPort {

    suspend fun create(personToSave: PersonToSave): Person
}
