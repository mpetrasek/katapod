#!/bin/bash
java -Dorg.slf4j.simpleLogger.defaultLogLevel=debug -jar ${project.artifactId}-${project.version}.jar "$@"
