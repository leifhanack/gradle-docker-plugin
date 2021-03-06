package de.gesellix.gradle.docker.tasks

import de.gesellix.docker.client.DockerClient
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class DockerExecTaskSpec extends Specification {

  def project
  def task
  def dockerClient = Mock(DockerClient)

  def setup() {
    project = ProjectBuilder.builder().build()
    task = project.task('dockerExec', type: DockerExecTask)
    task.dockerClient = dockerClient
  }

  def "delegates plain exec command via 'sh  -c' to dockerClient and saves result"() {
    given:
    def containerId = 'foo'
    task.containerId = containerId

    def commandLine = 'echo "foo" > /bar.txt && cat /bar.txt'
    task.commandLine = commandLine

    when:
    task.execute()

    then:
    1 * dockerClient.createExec(containerId, [
        "AttachStdin" : false,
        "AttachStdout": true,
        "AttachStderr": true,
        "Tty"         : false,
        "Cmd"         : ["sh", "-c", commandLine]
    ]) >> [Id: "exec-id"]

    1 * dockerClient.startExec("exec-id", [
        "Detach": false,
        "Tty"   : false]) >> ["some exec result"]

    and:
    task.result == ["some exec result"]
  }

  def "delegates exec commands to dockerClient and saves result"() {
    given:
    def containerId = 'foo'
    task.containerId = containerId

    def commands = ['sh', '-c', 'echo "foo" > /baz.txt && cat /baz.txt']
    task.commandLine = commands

    when:
    task.execute()

    then:
    1 * dockerClient.createExec(containerId, [
        "AttachStdin" : false,
        "AttachStdout": true,
        "AttachStderr": true,
        "Tty"         : false,
        "Cmd"         : commands
    ]) >> [Id: "exec-id"]

    1 * dockerClient.startExec("exec-id", [
        "Detach": false,
        "Tty"   : false]) >> ["some exec result"]

    and:
    task.result == ["some exec result"]
  }
}
