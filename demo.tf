terraform {
  required_version = ">= 1.0.0"
}

resource "null_resource" "hello" {
  provisioner "local-exec" {
    command = "echo Hello, World!"
  }
}