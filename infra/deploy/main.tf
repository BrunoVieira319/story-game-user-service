provider "google" {
  project     = var.project_id
  credentials = var.account_file
  region      = var.region
  version     = "2.14"
}

resource "google_compute_instance" "story-instance" {
  name         = "story-service"
  machine_type = var.machine_type
  zone         = var.zone
  tags         = ["http"]
  boot_disk {
    auto_delete = true
    initialize_params {
      image     = var.image_name
    }
  }
  network_interface {
    network = "default"
    access_config {
    }
  }
}

resource "google_compute_instance_group" "story-ig" {
  name      = "story-instance-group"
  instances = [google_compute_instance.story-instance.self_link]
  zone      = var.zone

  named_port {
    name = "http"
    port = 8082
  }
}

resource "google_compute_firewall" "story-firewall" {
  name    = "story-firewall"
  network = "default"

  allow {
    protocol = "tcp"
    ports    = ["80", "8082"]
  }
  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["http"]
}

resource "google_compute_backend_service" "story-service" {
  name          = "story-service"
  protocol      = "HTTP"
  port_name     = "http"
  health_checks = [google_compute_http_health_check.nlb-hc.self_link]

  backend {
    group = google_compute_instance_group.story-ig.self_link
  }
}

resource "google_compute_http_health_check" "nlb-hc" {
  name               = "load-balancer-health-check"
  request_path       = "/v1/users/health"
  port               = 8082
  check_interval_sec = 10
  timeout_sec        = 3
}

resource "google_compute_target_http_proxy" "story-proxy" {
  name    = "story-proxy"
  url_map = google_compute_url_map.story-url-map.self_link
}

resource "google_compute_global_forwarding_rule" "story-nlb" {
  name                  = "story-network-load-balancer"
  target                = google_compute_target_http_proxy.story-proxy.self_link
  load_balancing_scheme = "EXTERNAL"
  port_range            = "80"
}

resource "google_compute_url_map" "story-url-map" {
  name            = "story-url-map"
  default_service = google_compute_backend_service.story-service.self_link
}

resource "google_storage_bucket" "state-bucket" {
  name = "bards-poems-bucket"
}

terraform {
  backend "gcs" {
    bucket  = google_storage_bucket.state-bucket.name
    prefix  = "user/terraform/state"
  }
}

data "terraform_remote_state" "state" {
  backend = "gcs"
  config = {
    bucket = "bards-poems"
    prefix  = "user/terraform/state"
  }
}