variable "project_id" {
  description = "The ID of the Google Cloud project"
}

variable "account_file" {
  description = "Path to your credentials file"
}

variable "region" {
  default = "us-central1"
}

variable "zone" {
  default = "us-central1-a"
}

variable "machine_type" {
  default     = "g1-small"
  description = "GCP machine type"
}

variable "image_name" {
  description = "Name of the image to be launched"
}