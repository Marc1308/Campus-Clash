# Campus Clash

A Scala-based console game where players explore campus buildings, visit rooms and complete quests.

## Build Status

[![Scala CI](https://github.com/Marc1308/Campus-Clash/actions/workflows/scala.yml/badge.svg?branch=development)](https://github.com/Marc1308/Campus-Clash/actions)

## Code Coverage

[![Coverage Status](https://coveralls.io/repos/github/Marc1308/Campus-Clash/badge.svg?branch=development)](https://coveralls.io/github/Marc1308/Campus-Clash?branch=development)

## Technologies

- Scala 3
- sbt
- ScalaTest
- GitHub Actions
- Scoverage
- Coveralls
- Stryker4s

## Testing

Run all tests:

```bash
sbt clean test
```

Generate code coverage:

```bash
sbt clean coverage test coverageReport
```

Run mutation testing:

## Mutation Testing

Mutation testing is performed using Stryker4s.

| Metric | Result |
|---|---:|
| Total Mutants | 102 |
| Killed | 102 |
| Survived | 0 |
| No Coverage | 0 |
| Mutation Score | 100% |

Run mutation testing locally:

```bash
sbt stryker
```