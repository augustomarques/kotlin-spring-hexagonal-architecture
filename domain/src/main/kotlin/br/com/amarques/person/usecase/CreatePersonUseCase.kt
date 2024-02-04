package br.com.amarques.person.usecase

import br.com.amarques.person.model.PersonToSave
import br.com.amarques.person.ports.driven.PersonDataAccessPort
import br.com.amarques.person.ports.driver.CreatePersonPort
import br.com.amarques.shared.UseCase

@UseCase
class CreatePersonUseCase(
    private val personDataAccessPort: PersonDataAccessPort
) : CreatePersonPort {

    override suspend fun create(personToSave: PersonToSave) =
        personDataAccessPort.save(personToSave)
}
