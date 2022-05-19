package com.infinityworks.flutter.jonathancowling;

import software.constructs.Construct;

import java.util.List;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.kinesis.Stream;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.ApplicationCodeConfigurationProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.ApplicationConfigurationProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.ApplicationSnapshotConfigurationProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.CheckpointConfigurationProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.CodeContentProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.EnvironmentPropertiesProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.FlinkApplicationConfigurationProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.MonitoringConfigurationProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.ParallelismConfigurationProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.PropertyGroupProperty;
import software.amazon.awscdk.services.kinesisanalytics.CfnApplicationV2.S3ContentLocationProperty;
import software.amazon.awscdk.services.ssm.StringParameter;

public class FlinkStack extends Stack {
        public FlinkStack(final Construct scope, final String id) {
                this(scope, id, null);
        }

        public FlinkStack(final Construct scope, final String id, final StackProps props) {
                super(scope, id, props);

                Stream input = Stream.Builder.create(this, "input-stream")
                                .streamName("input-stream")
                                .shardCount(1)
                                .build();


                Role appRole = Role.Builder.create(this, "kinesis-app-role")
                                .assumedBy(
                                        new ServicePrincipal("kinesisanalytics.amazonaws.com")
                                )
                                .managedPolicies(List.of(
                                // TODO add more policies
                                ))
                                .build();

                CfnApplicationV2.Builder.create(this, "kinesis-app")
                        .runtimeEnvironment("FLINK_1_13")
                        .serviceExecutionRole(appRole.getRoleId())
                        .applicationName("kinesis-app")
                        .applicationConfiguration(ApplicationConfigurationProperty.builder()
                                .applicationCodeConfiguration(ApplicationCodeConfigurationProperty.builder()
                                        .codeContent(CodeContentProperty.builder()
                                                .s3ContentLocation(S3ContentLocationProperty.builder()
                                                        .bucketArn("bucketArn")
                                                        .fileKey("fileKey")
                                                        .objectVersion("objectVersion")
                                                        .build())
                                                .textContent("textContent")
                                                .zipFileContent("zipFileContent")
                                                .build())
                                        .codeContentType("codeContentType")
                                        .build())
                                .applicationSnapshotConfiguration(ApplicationSnapshotConfigurationProperty.builder()
                                        .snapshotsEnabled(false)
                                        .build())
                                .environmentProperties(EnvironmentPropertiesProperty.builder()
                                        .propertyGroups(List.of(PropertyGroupProperty.builder()
                                                .propertyGroupId("propertyGroupId")
                                                .propertyMap(propertyMap)
                                                .build()))
                                        .build())
                                .flinkApplicationConfiguration(FlinkApplicationConfigurationProperty.builder()
                                        .checkpointConfiguration(CheckpointConfigurationProperty.builder()
                                                .configurationType("configurationType")
                                                // the properties below are optional
                                                .checkpointingEnabled(false)
                                                .checkpointInterval(123)
                                                .minPauseBetweenCheckpoints(123)
                                                .build())
                                        .monitoringConfiguration(MonitoringConfigurationProperty.builder()
                                                .configurationType("configurationType")
                                                // the properties below are optional
                                                .logLevel("logLevel")
                                                .metricsLevel("metricsLevel")
                                                .build())
                                        .parallelismConfiguration(ParallelismConfigurationProperty.builder()
                                                .configurationType("configurationType")
                                                // the properties below are optional
                                                .autoScalingEnabled(false)
                                                .parallelism(123)
                                                .parallelismPerKpu(123)
                                                .build())
                                        .build())
                                .build());
                                

                // Application flinkApp = Application.Builder.create(this, "App")
                //                 .code(ApplicationCode.fromAsset(join(__dirname, "code-asset")))
                //                 .runtime(Runtime.FLINK_1_11)
                //                 .build();
                //         Alarm.Builder.create(stack, "Alarm")
                //                 .metric(flinkApp.metricFullRestarts())
                //                 .evaluationPeriods(1)
                //                 .threshold(3)
                //                 .build();
                //         app.synth();
        }
}
