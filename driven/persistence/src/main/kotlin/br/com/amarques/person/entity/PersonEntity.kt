package br.com.amarques.person.entity

import br.com.amarques.person.model.Person
import br.com.amarques.person.model.PersonToSave
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "person")
data class PersonEntity(

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:Column(name = "name", nullable = false)
    val name: String
) {

    fun merge(sponsorToSave: PersonToSave) =
        copy(
            name = sponsorToSave.name
        )

    fun toModel() =
        Person(
            id = id!!,
            name = name
        )
}

fun PersonToSave.toEntity() =
    PersonEntity(
        name = name
    )
