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
