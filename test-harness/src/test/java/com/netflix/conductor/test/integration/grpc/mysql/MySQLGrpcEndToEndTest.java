/*
 * Copyright 2020 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.test.integration.grpc.mysql;

import com.netflix.conductor.client.grpc.MetadataClient;
import com.netflix.conductor.client.grpc.TaskClient;
import com.netflix.conductor.client.grpc.WorkflowClient;
import com.netflix.conductor.dao.IndexDAO;
import com.netflix.conductor.test.integration.grpc.AbstractGrpcEndToEndTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
    "db=mysql",
    "conductor.grpc.server.port=8094",
    "jdbc.url=jdbc:mysql://localhost:33307/conductor?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
    "jdbc.username=root",
    "jdbc.password=root",
    "conductor.mysql.connection.pool.size.min=8",
    "conductor.mysql.connection.pool.size.max=8",
    "conductor.mysql.connection.pool.idle.min=300000",
    "spring.flyway.enabled=false"
})
public class MySQLGrpcEndToEndTest extends AbstractGrpcEndToEndTest {

    @Autowired
    private IndexDAO indexDAO;

    @BeforeClass
    public static void setup() {
        container.start();

        String httpHostAddress = container.getHttpHostAddress();
        System.setProperty("workflow.elasticsearch.url", "http://" + httpHostAddress);
    }

    @AfterClass
    public static void cleanup() {
        container.stop();
    }

    @Before
    public void init() throws Exception {
        indexDAO.setup();

        taskClient = new TaskClient("localhost", 8094);
        workflowClient = new WorkflowClient("localhost", 8094);
        metadataClient = new MetadataClient("localhost", 8094);
    }
}
