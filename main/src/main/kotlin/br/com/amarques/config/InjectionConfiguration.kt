package br.com.amarques.config

import br.com.amarques.shared.UseCase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = ["br.com.amarques"],
    includeFilters = [ComponentScan.Filter(UseCase::class)]
)
open class InjectionConfiguration
