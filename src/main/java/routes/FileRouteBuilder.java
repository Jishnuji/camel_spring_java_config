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
package routes;

import exceptions.IllegalFileFormatException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import processors.MessageCounter;
import processors.StatisticMessage;

@Component
public class FileRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(IllegalFileFormatException.class)
                .handled(true)
                .setHeader("FailedBecause", exceptionMessage())
                .to("jms:invalid.queue")
                .log(LoggingLevel.ERROR, "${header.FailedBecause}." + " Error occurred in " + "${file:name}");

        from("{{from}}")
                .transacted()
                    .process(new MessageCounter())
                    .choice()
                    .when(header("CamelFileName").endsWith(".xml"))
                        .to("jms:xml.queue")
                    .when(header("CamelFileName").endsWith(".txt"))
                        .to("jms:txt.queue")
                        .setBody(simple("'${body}'"))
                        .to("sql:{{query}}")
                    .otherwise()
                        .throwException(new IllegalFileFormatException("Routed file must be in text or xml only"))
                .end()
                    .filter(header("StatMsg").isEqualTo(true))
                        .process(new StatisticMessage())
                        .setHeader("subject", simple("File statistics message"))
                        .to("{{email.uri}}");
    }
}
