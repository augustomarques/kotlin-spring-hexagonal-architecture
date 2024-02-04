package br.com.amarques.person.exception

class PersonNotFoundException(id: Long) : RuntimeException("Person [id $id] not found")
