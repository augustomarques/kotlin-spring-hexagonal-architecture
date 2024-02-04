package br.com.amarques.person.ports.driver

import br.com.amarques.person.model.Person

interface GetAllPersonsPort {

    suspend fun getAll(): List<Person>
}
