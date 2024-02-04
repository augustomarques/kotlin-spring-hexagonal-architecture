package br.com.amarques.person.usecase

import br.com.amarques.person.ports.driven.PersonDataAccessPort
import br.com.amarques.person.ports.driver.GetAllPersonsPort
import br.com.amarques.shared.UseCase

@UseCase
class GetAllPersonsUseCase(
    private val personDataAccessPort: PersonDataAccessPort
) : GetAllPersonsPort {

    override suspend fun getAll() =
        personDataAccessPort.getAll()
}
