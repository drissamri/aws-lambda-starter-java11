package com.drissamri.favorites;


import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder;
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.DescribeStacksResult;
import com.amazonaws.services.cloudformation.model.Output;
import com.amazonaws.services.cloudformation.model.Stack;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

public class AddFavoriteLambdaIT {
    private static Logger LOG = LoggerFactory.getLogger(AddFavoriteLambdaIT.class);

    private static String stackName;

    @Test
    public void shouldStoreFavoriteSuccessfully() {
        stackName = System.getProperty("stackName");
        LOG.info("Stack name used: {}", stackName);

        if (stackName == null) {
            fail("stackName property must be set");
        }

        String endpointUrl = this.resolveEndpointUrl();
        JSONObject input = new JSONObject()
                .put("name", "integration-driss");

        //@formatter:off
        given()
                .contentType("application/json")
                .accept("application/json")
                .body(input.toString())
        .when()
                .post(endpointUrl)
        .then()
                .statusCode(200)
                .body("name", equalTo("integration-driss"));
        //@formatter:on
    }

    /**
     * Get API URL from the CloudFormation stack Output defined in AWS SAM (template.yaml)
     */
    private String resolveEndpointUrl() {
        AmazonCloudFormation cfn = AmazonCloudFormationClientBuilder.defaultClient();

        DescribeStacksRequest request = new DescribeStacksRequest()
                .withStackName(stackName);

        DescribeStacksResult describeStacksResult = cfn.describeStacks(request);
        Stack stack = describeStacksResult.getStacks().get(0);
        Output output = stack.getOutputs().get(0);
        String endpointUrl = output.getOutputValue();

        LOG.info("Endpoint found: {}", endpointUrl);
        return endpointUrl;
    }
}