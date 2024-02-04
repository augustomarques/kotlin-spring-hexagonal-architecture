package br.com.amarques.person.controller

import br.com.amarques.person.dto.CreatePersonDTO
import br.com.amarques.person.dto.PersonDTO
import br.com.amarques.person.dto.toDTO
import br.com.amarques.person.ports.driver.CreatePersonPort
import br.com.amarques.person.ports.driver.GetAllPersonsPort
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@Tag(name = "Persons")
@RestController
@RequestMapping("/persons")
class PersonController(
    private val createPersonPort: CreatePersonPort,
    private val getAllPersonsPort: GetAllPersonsPort
) {

    @PostMapping
    @Operation(summary = "Register a new person")
    suspend fun create(
        @RequestBody @Valid
        createPersonDTO: CreatePersonDTO,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<PersonDTO> {
        val personDTO = createPersonPort.create(createPersonDTO.toSave()).toDTO()
        val uri = uriComponentsBuilder.path("/persons/${personDTO.id}").build().toUri()

        return ResponseEntity.created(uri).body(personDTO)
    }

    @GetMapping
    @Operation(summary = "Get all persons")
    suspend fun getAll() =
        ResponseEntity.ok(getAllPersonsPort.getAll().map { it.toDTO() })
}
