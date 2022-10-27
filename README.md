# iText-poc
[![Java CI with Maven][github-image]][github-url-main] [![Sonar quality gate][sonar-quality-gate]][sonar-url] [![Sonar coverage][sonar-coverage]][sonar-url] [![Sonar bugs][sonar-bugs]][sonar-url] [![Sonar vulnerabilities][sonar-vulnerabilities]][sonar-url] [![MIT licensed][mit-badge]](./LICENSE.txt)

This repository holds examples for testing/validation of [iText 7](https://github.com/itext/itext7) features.

## Pre-requisities
* JDK 17
* Maven 3.8
* Lombok (installed into the IDE)

## Used Technologies

| Area          | Tool                  | Description / Usage                      |
| ----------    | --------------------- | ---------------------------------------- |
| **General**   |                       |                                          |
|               | iText                 | PDF generation                           |
|               | Lombok                | Simplification of Java classes           |
|               | Spring Boot           | Just for dependency management           |
|               | Lorem                 | Library for LoremIpsum texts             |
| **Testing**   |                       |                                          |
|               | JUnit                 | Unit testing with JUnit5                 |
|               | AssertJ               | Assertions with Fluent API               |

[github-url-main]: https://github.com/arnosthavelka/itext-poc/actions/workflows/maven.yml
[github-image]: https://github.com/arnosthavelka/itext-poc/actions/workflows/maven.yml/badge.svg

[sonar-url]: https://sonarcloud.io/dashboard?id=arnosthavelka_itext-poc
[sonar-quality-gate]: https://sonarcloud.io/api/project_badges/measure?project=arnosthavelka_itext-poc&metric=alert_status
[sonar-coverage]: https://sonarcloud.io/api/project_badges/measure?project=arnosthavelka_itext-poc&metric=coverage
[sonar-bugs]: https://sonarcloud.io/api/project_badges/measure?project=arnosthavelka_itext-poc&metric=bugs
[sonar-vulnerabilities]: https://sonarcloud.io/api/project_badges/measure?project=arnosthavelka_itext-poc&metric=vulnerabilities
[mit-badge]: https://img.shields.io/badge/license-MIT-maroon.svg
