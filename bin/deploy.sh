STACK_NAME=${1:-'favorites-service-dev'}
REGION=eu-central-1
S3_DEPLOY_BUCKET=telenet-ent-cbe-deployments

echo "Start deploy stack:" $STACK_NAME
sam deploy \
  --s3-bucket $S3_DEPLOY_BUCKET \
  --stack-name $STACK_NAME \
  --capabilities CAPABILITY_IAM \
  --region $REGION



