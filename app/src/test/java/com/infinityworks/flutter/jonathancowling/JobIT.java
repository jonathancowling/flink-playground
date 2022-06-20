package com.infinityworks.flutter.jonathancowling;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Map;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.client.deployment.StandaloneClusterId;
import org.apache.flink.client.program.PackagedProgram;
import org.apache.flink.client.program.PackagedProgramUtils;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobmaster.JobResult;
import org.apache.flink.util.SerializedThrowable;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class JobIT {

    @Container
    public static DockerComposeContainer<?> compose = new DockerComposeContainer<>(
            new File("src/test/resources/compose/integration.yml"))
            .withExposedService("jobmanager", 8081, new HostPortWaitStrategy());

    @Test
    public void test() throws Exception {
        Configuration clientConfig = ParameterTool.fromMap(Map.of(
                JobManagerOptions.ADDRESS.key(), compose.getServiceHost("jobmanager", 8081),
                RestOptions.PORT.key(), Integer.valueOf(compose.getServicePort("jobmanager", 8081)).toString()))
                .getConfiguration();

        try (RestClusterClient<StandaloneClusterId> c = new RestClusterClient<>(clientConfig,
                StandaloneClusterId.getInstance())) {

            PackagedProgram program = PackagedProgram.newBuilder()
                    .setJarFile(new File("target/flink-app.jar"))
                //     .setArguments(args)
                    .build();

            JobGraph job = PackagedProgramUtils.createJobGraph(
                    program,
                    clientConfig,
                    1,
                    false);

            c.submitJob(job).get();

            JobResult res = c.requestJobResult(job.getJobID()).join();
            assertNull(
                    res.getSerializedThrowable().map(SerializedThrowable::getFullStringifiedStackTrace).orElse(null));
            assertTrue(res.isSuccess());
        }
    }

}
