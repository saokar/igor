/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.igor

import com.netflix.config.ConfigurationManager
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Application entry point.
 */
@Configuration
@EnableAutoConfiguration(exclude = [GroovyTemplateAutoConfiguration])
@EnableConfigurationProperties(IgorConfigurationProperties)
@ComponentScan(['com.netflix.spinnaker.igor', 'com.netflix.spinnaker.config', 'com.netflix.spinnaker.igor.health'])
class Main extends SpringBootServletInitializer {

    static final Map<String, String> DEFAULT_PROPS = [
        'netflix.environment'    : 'test',
        'netflix.account'        : '${netflix.environment}',
        'netflix.stack'          : 'test',
        'spring.config.location' : '${user.home}/.spinnaker/',
        'spring.application.name': 'igor',
        'spring.config.name'     : 'spinnaker,${spring.application.name}',
        'spring.profiles.active' : '${netflix.environment},local'
    ]

    static {
        ConfigurationManager.loadCascadedPropertiesFromResources("hystrix")
    }

    static void main(String... args) {
        new SpringApplicationBuilder().properties(DEFAULT_PROPS).sources(Main).run(args)
    }

    @Override
    SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.properties(DEFAULT_PROPS).sources(Main)
    }
}
