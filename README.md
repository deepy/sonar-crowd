# Sonar Crowd Plugin

This plugin allows the delegation of SonarQube authentication and authorization to Atlassian Crowd. 
The previous version of this plugin has been changed to provide the same functionality as the SonarQube LDAP plugin:

* Password checking against the external authentication engine.
* Automatic synchronization of usernames and emails.
* Automatic synchronization of relationships between users and groups (authorization).
* Ability to authenticate against both the external and the internal authentication systems 
(for instance, technical SonarQube user accounts do not need to be defined in Crowd as there is an automatic 
fallback on SonarQube engine if the user is not defined in Crowd or if the Crowd server is down).

During the first authentication trial, if the password is correct, the SonarQube database is automatically 
populated with the new user. Each time a user logs into SonarQube, the username, the email and the 
groups this user belongs to that are refreshed in the SonarQube database.

# Requirements

This plugin requires Atlassian Crowd 2.1.0 or later.

# Usage

1. Configure the crowd plugin by editing the _SONARQUBE_HOME/conf/sonar.properties_ file
1. Restart the SonarQube server and check the log file for:
    org.sonar.INFO  Security realm: Crowd
    ...
    o.s.p.c.CrowdRealm  Crowd configuration is valid, connection test successful.
1. Log into SonarQube

# Configuration

| Property          | Description                          | Default value | Mandatory | Example                     |
|-------------------|--------------------------------------|---------------|-----------|-----------------------------|
| crowd.url         | The base URL of the crowd server     | None          | Yes       | http://localhost:8095/crowd |
| crowd.application | The name of the application in crowd | sonar         | No        | sonar                       |
| crowd.password    | The password of the application in crowd | None      | Yes       | secret                      |
| sonar.security.realm | Authenticate against the external system. Set to `Crowd` | None | No | Crowd | 
