package de.gesellix.gradle.docker.tasks

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DockerDisposeContainerTask extends AbstractDockerTask {

  private static Logger logger = LoggerFactory.getLogger(DockerDisposeContainerTask)

  @Input
  def containerId
  @Input
  @Optional
  def rmiParentImage = false

  DockerDisposeContainerTask() {
    description = "Stops and removes a container and optionally its parent image"
    group = "Docker"
  }

  @TaskAction
  def dispose() {
    logger.info "docker dispose"

    def containerId = getContainerId()
    def containerDetails = getDockerClient().inspectContainer(containerId)
    getDockerClient().stop(containerId)
    getDockerClient().wait(containerId)
    getDockerClient().rm(containerId)
    if (getRmiParentImage()) {
      getDockerClient().rmi(containerDetails.content.Image)
    }
  }
}
