package de.gesellix.gradle.docker.tasks

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DockerStartTask extends AbstractDockerTask {

  private static Logger logger = LoggerFactory.getLogger(DockerStopTask)

  @Input
  def containerId

  def result

  DockerStartTask() {
    description = "Start a stopped container"
    group = "Docker"
  }

  @TaskAction
  def start() {
    logger.info "docker start"
    result = getDockerClient().startContainer(getContainerId())
    return result
  }
}
