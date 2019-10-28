output "load-balancer-ip" {
  value = google_compute_global_forwarding_rule.story-nlb.ip_address
}

output "story-service-ip" {
  value = google_compute_instance.story-instance.network_interface.0.access_config.0.nat_ip
}