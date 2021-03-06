/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.processor;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;

public class GenericXmlTokenizeTest extends ContextTestSupport {

    @Test
    public void testSendClosedTagMessageToTokenize() throws Exception {
        String message = "<?xml version='1.0' encoding='UTF-8' ?><parent><child anotherAttr='' some_attr=''></child></parent>";
        // the result is formated
        String expectedMessage = "<child anotherAttr=\"\" some_attr=\"\"/>";

        MockEndpoint resultEndpoint = getMockEndpoint("mock:result");
        resultEndpoint.expectedBodiesReceived(expectedMessage);

        template.sendBody("direct:start", message);

        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void testSendSelfClosingTagMessageToTokenize() throws Exception {
        String message
                = "<?xml version='1.0' encoding='UTF-8' ?><parent xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'><child some_attr='' anotherAttr='' /></parent>";
        // the result is formated
        String expectedMessage = "<child anotherAttr=\"\" some_attr=\"\"/>";

        MockEndpoint resultEndpoint = getMockEndpoint("mock:result");
        resultEndpoint.expectedBodiesReceived(expectedMessage);

        template.sendBody("direct:start", message);

        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start").split().xpath("//child").to("mock:result");
            }
        };
    }

}
