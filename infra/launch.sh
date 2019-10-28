#!/usr/bin/env bash

ZONE="us-central1-a"
SOURCE_IMAGE="ubuntu-1804-bionic-v20190813a"
PROJECT_ID=$1
ACCOUNT_FILE=$2
DATE=$(date +%s)
IMAGE_NAME="bards-poems-user-service-1572228171"

function provision() {
    cd provisioning/packer/ || exit
    sudo packer build \
        -var "zone=$ZONE" \
        -var "project_id=$PROJECT_ID" \
        -var "source_image=$SOURCE_IMAGE" \
        -var "account_file=$ACCOUNT_FILE" \
        -var "image_name=$IMAGE_NAME" \
         gcp-bards.json
    cd ../../
}

function launch() {
    cd deploy/ || exit
    terraform init
    terraform apply -auto-approve \
        -var "project_id=$PROJECT_ID" \
        -var "account_file=$ACCOUNT_FILE" \
        -var "image_name=$IMAGE_NAME"
    cd ..
}

launch