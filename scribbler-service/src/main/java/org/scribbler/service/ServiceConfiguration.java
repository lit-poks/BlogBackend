package org.scribbler.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * Enabling the component scan and entity scan of classes in the below mentioned
 *           "org.scribbler.service"
 *           "org.scribbler.service.entity"
 * packages respectively.
 */

@Configuration
@ComponentScan("org.scribbler.service")
@EntityScan("org.scribbler.service.entity")
public class ServiceConfiguration {
}
