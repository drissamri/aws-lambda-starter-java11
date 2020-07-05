package com.drissamri.favorites;


import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder;
import com.amazonaws.services.cloudformation.model.DescribeStackResourceRequest;
import com.amazonaws.services.cloudformation.model.DescribeStackResourceResult;
import com.amazonaws.util.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.ServiceException;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class AddFavoriteLambdaIT {
    private static final LambdaClient LAMBDA_CLIENT = LambdaClient.builder()
            .httpClient(UrlConnectionHttpClient.builder().build())
            .region(Region.EU_CENTRAL_1).build();
    private static String stackName;

    @BeforeAll
    public static void setUp() {
        stackName = "favorites-service-stack";
        if (stackName == null) {
            throw new RuntimeException("stackName property must be set");
        }
    }

    @Test
    public void shouldStoreFavoriteSuccessfully() throws IOException {
        String inputPayload = readStringFromFile("resources/input.json");
        InvokeResponse result = invokeLambda("AddFavoriteLambda", inputPayload);
        assertThat(result.statusCode()).isEqualTo(200);
        assertThat(result.payload().asUtf8String()).contains("Favorite:");
    }

    /**
     * Utility Method to invoke AWS Lambda using the AWS SDK
     *
     * @param lambdaLogicalName
     * @param payload
     * @return Output string
     */
    private InvokeResponse invokeLambda(String lambdaLogicalName, String payload) {
        InvokeRequest request = InvokeRequest.builder()
                .functionName(resolvePhysicalId(lambdaLogicalName))
                .payload(SdkBytes.fromUtf8String(payload))
                .build();
        try {
            return LAMBDA_CLIENT.invoke(request);
        } catch (ServiceException e) {
            throw new RuntimeException("Invoking failed: " + e.getMessage());
        }
    }

    private String readStringFromFile(String file) throws IOException {
        InputStream eventStream = this.getClass().getClassLoader().getResourceAsStream(file);
        return IOUtils.toString(eventStream);
    }

    /**
     * Get Physical AWS ID based on the Logical SAM/CloudFormation name defined in template.yaml
     *
     * @param logicalId
     * @return Physical AWS resource ID
     */
    private String resolvePhysicalId(String logicalId) {
        AmazonCloudFormation cfn = AmazonCloudFormationClientBuilder.defaultClient();

        DescribeStackResourceRequest request = new DescribeStackResourceRequest()
                .withStackName(stackName)
                .withLogicalResourceId(logicalId);

        DescribeStackResourceResult result = cfn.describeStackResource(request);
        return result.getStackResourceDetail().getPhysicalResourceId();
    }
}