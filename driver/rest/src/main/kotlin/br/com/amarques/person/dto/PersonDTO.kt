package br.com.amarques.person.dto

import br.com.amarques.person.model.Person

data class PersonDTO(
    val id: Long,
    val name: String
)

fun Person.toDTO() =
    PersonDTO(
        id = id,
        name = name
    )
