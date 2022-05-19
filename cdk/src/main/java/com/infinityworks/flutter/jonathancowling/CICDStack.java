package com.infinityworks.flutter.jonathancowling;

import software.constructs.Construct;

import java.util.List;
import java.util.Map;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.FederatedPrincipal;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.iam.OpenIdConnectProvider;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.ssm.StringParameter;

public class CICDStack extends Stack {
    public CICDStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CICDStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here

        // example resource
        // final Queue queue = Queue.Builder.create(this, "CdkQueue")
        // .visibilityTimeout(Duration.seconds(300))
        // .build();

        OpenIdConnectProvider provider = OpenIdConnectProvider.Builder.create(this, "MyProvider")
                .url("https://token.actions.githubusercontent.com")
                .clientIds(List.of("sts.amazonaws.com"))
                .thumbprints(List.of(
                        "6938fd4d98bab03faadb97b34396831e3780aea1" // GitHub's thumbprint
                ))
                .build();

        StringParameter.Builder.create(this, "oidc-arn")
                .parameterName("arn/oidc")
                .stringValue(provider.getOpenIdConnectProviderArn());

        List<String> repos = (List<String>) scope.getNode().tryGetContext("repos");

        Role ciRole = Role.Builder.create(this, "oidc-role")
                .assumedBy(
                        new FederatedPrincipal(provider.getOpenIdConnectProviderArn(),
                                Map.of("StringLike", Map.of(
                                        "token.actions.githubusercontent.com:aud", "sts.amazonaws.com",
                                        "token.actions.githubusercontent.com:sub",
                                        repos.stream().map(repo -> "repo:" + repo + ":*")))))
                .managedPolicies(List.of(
                // TODO add more policies
                ))
                .build();
        
        StringParameter.Builder.create(this, "ci-role-arn")
                .parameterName("arn/ci-role")
                .stringValue(provider.getOpenIdConnectProviderArn());
    }
}
