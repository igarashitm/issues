/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.quickstarts.bean.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;

@Named("PlainBean")
public class PlainBean {

    public void foobar() {
        System.out.println("nein");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("************************************************");
        System.out.println("****** @PostConstruct method is invoked on PlainBean");
        System.out.println("************************************************");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("************************************************");
        System.out.println("****** @PreDestroy method is invoked on PlainBean");
        System.out.println("************************************************");
    }
}
