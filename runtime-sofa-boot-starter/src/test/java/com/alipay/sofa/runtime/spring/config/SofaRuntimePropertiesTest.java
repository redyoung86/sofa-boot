/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.runtime.spring.config;

import com.alipay.sofa.runtime.SofaRuntimeProperties;
import com.alipay.sofa.runtime.spring.configuration.SofaRuntimeAutoConfiguration;
import com.alipay.sofa.runtime.spring.listener.SofaRuntimeApplicationListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class SofaRuntimePropertiesTest {

    private final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

    @After
    public void closeContext() {
        this.applicationContext.close();
    }

    @Before
    public void before() {
        ApplicationPreparedEvent applicationPreparedEvent = Mockito
            .mock(ApplicationPreparedEvent.class);
        when(applicationPreparedEvent.getApplicationContext()).thenReturn(applicationContext);
        new SofaRuntimeApplicationListener().onApplicationEvent(applicationPreparedEvent);
    }

    @Test
    public void testDisableJvmFirstProperty() {
        assertFalse(SofaRuntimeProperties.isDisableJvmFirst(applicationContext.getClassLoader()));
        TestPropertyValues.of("com.alipay.sofa.boot.disableJvmFirst=true").applyTo(
            applicationContext);
        this.applicationContext.register(SofaRuntimeAutoConfiguration.class);
        this.applicationContext.refresh();
        SofaRuntimeConfigurationProperties configurationProperties = this.applicationContext
            .getBean(SofaRuntimeConfigurationProperties.class);

        assertTrue(SofaRuntimeProperties.isDisableJvmFirst(applicationContext.getClassLoader()));
        assertTrue(configurationProperties.isDisableJvmFirst());
    }

    @Test
    public void testSkipJvmReferenceHealthCheckProperty() {
        assertFalse(SofaRuntimeProperties.isSkipJvmReferenceHealthCheck(applicationContext
            .getClassLoader()));

        TestPropertyValues.of("com.alipay.sofa.boot.skipJvmReferenceHealthCheck=true").applyTo(
            applicationContext);
        this.applicationContext.register(SofaRuntimeAutoConfiguration.class);
        this.applicationContext.refresh();
        SofaRuntimeConfigurationProperties configurationProperties = this.applicationContext
            .getBean(SofaRuntimeConfigurationProperties.class);

        assertTrue(SofaRuntimeProperties.isSkipJvmReferenceHealthCheck(applicationContext
            .getClassLoader()));
        assertTrue(configurationProperties.isSkipJvmReferenceHealthCheck());
    }

}