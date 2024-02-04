package br.com.amarques.person.dto

import br.com.amarques.person.model.PersonToSave

data class CreatePersonDTO(
    val name: String
) {
    fun toSave() =
        PersonToSave(
            name = name
        )
}
