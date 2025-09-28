#!/bin/bash

mkdir -p ~/.m2

if [[ -n "$MAVEN_ENT_PASSWORD" ]]; then
  export MAVEN_ENT_PASSWORD=$(echo "$MAVEN_ENT_PASSWORD" | base64 -d)
fi

cat <<EOF > ~/.m2/settings.xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0">
  <servers>
    <server>
      <id>nexus-deptinfo</id>
      <username>${MAVEN_ENT_USERNAME}</username>
      <password>${MAVEN_ENT_PASSWORD}</password>
    </server>
    <server>
      <id>nexus-deptinfo-snapshots</id>
      <username>${MAVEN_ENT_USERNAME}</username>
      <password>${MAVEN_ENT_PASSWORD}</password>
    </server>
    <server>
      <id>sonar</id>
      <username>${MAVEN_ENT_USERNAME}</username>
      <password>${MAVEN_ENT_PASSWORD}</password>
    </server>
  </servers>

  <mirrors>
    <mirror>
      <id>nexus-deptinfo</id>
      <mirrorOf>*</mirrorOf>
      <url>https://disc.univ-fcomte.fr/cr700-nexus/repository/maven-public/</url>
    </mirror>
  </mirrors>

  <pluginGroups>
    <pluginGroup>org.sonarsource.scanner.maven</pluginGroup>
  </pluginGroups>

  <profiles>
    <profile>
      <id>nexus-deptinfo-snapshots</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <repositories>
        <repository>
          <id>nexus-deptinfo-snapshots</id>
          <url>https://disc.univ-fcomte.fr/cr700-nexus/content/groups/public</url>
          <releases><enabled>true</enabled></releases>
          <snapshots><enabled>true</enabled></snapshots>
        </repository>
      </repositories>
    </profile>
    <profile>
      <id>sonar</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <sonar.host.url>https://disc.univ-fcomte.fr/cr700-sonarqube</sonar.host.url>
      </properties>
    </profile>
  </profiles>
</settings>
EOF
