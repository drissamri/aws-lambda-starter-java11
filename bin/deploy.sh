STACK_NAME=favorites-service-stack
REGION=eu-central-1
S3_DEPLOY_BUCKET=telenet-ent-cbe-deployments

sam deploy \
  --s3-bucket $S3_DEPLOY_BUCKET \
  --stack-name $STACK_NAME \
  --capabilities CAPABILITY_IAM \
  --region $REGION

export FAVORITE_API_ENDPOINT=$(aws cloudformation describe-stacks --stack-name $STACK_NAME \
     --query 'Stacks[0].Outputs[?OutputKey==`ApiEndpoint`].OutputValue' \
     --output text)



