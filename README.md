# iText-poc
[![Travis Build Status][travis-image]][travis-url-main] [![Sonar quality gate][sonar-quality-gate]][sonar-url] [![Sonar coverage][sonar-coverage]][sonar-url] [![Sonar bugs][sonar-bugs]][sonar-url] [![Sonar vulnerabilities][sonar-vulnerabilities]][sonar-url] [![MIT licensed][mit-badge]](./LICENSE.txt)

This repository holds examples for testing/validation of [iText 7](https://github.com/itext/itext7) features.

## Pre-requisities
* JDK 17
* Maven 3.6
* Lombok (installed into the IDE)

## Used Technologies

| Area          | Tool                  | Version      | Description / Usage                      |
| ----------    | --------------------- | ------------ | ---------------------------------------- |
| **General**   |                       |              |                                          |
|               | iText                 | 7.2.0        | PDF generation                           |
|               | Lombok                | by SB        | Simplification of Java classes           |
|               | Spring Boot           | 2.6.0        | Just for dependency management           |
|               | Lorem                 | 2.1          | Library for LoremIpsum texts             |
| **Testing**   |                       |              |                                          |
|               | JUnit                 | by SB        | Unit testing with JUnit5                 |
|               | AssertJ               | by SB        | Assertions with Fluent API               |

[travis-url-main]: https://app.travis-ci.com/github/arnosthavelka/itext-poc
[travis-image]: https://travis-ci.com/arnosthavelka/itext-poc.svg?branch=master

[sonar-url]: https://sonarcloud.io/dashboard?id=arnosthavelka_itext-poc
[sonar-quality-gate]: https://sonarcloud.io/api/project_badges/measure?project=arnosthavelka_itext-poc&metric=alert_status
[sonar-coverage]: https://sonarcloud.io/api/project_badges/measure?project=arnosthavelka_itext-poc&metric=coverage
[sonar-bugs]: https://sonarcloud.io/api/project_badges/measure?project=arnosthavelka_itext-poc&metric=bugs
[sonar-vulnerabilities]: https://sonarcloud.io/api/project_badges/measure?project=arnosthavelka_itext-poc&metric=vulnerabilities
[mit-badge]: https://img.shields.io/badge/license-MIT-maroon.svg
