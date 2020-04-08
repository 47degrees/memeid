# Changelog

## [Unreleased](https://github.com/47degrees/memeid/tree/HEAD)

[Full Changelog](https://github.com/47degrees/memeid/compare/v0.1...HEAD)

🚀 **Features**

- Compile regex statically in `fromString` [\#136](https://github.com/47degrees/memeid/pull/136) ([purrgrammer](https://github.com/purrgrammer))

🐛 **Bug Fixes**

- fromString method accepts invalid strings [\#134](https://github.com/47degrees/memeid/issues/134)
- Validate string before calling java.util.fromString [\#135](https://github.com/47degrees/memeid/pull/135) ([Philippus](https://github.com/Philippus))

📈 **Dependency updates**

- Update specs2-cats, specs2-scalacheck to 4.9.3 [\#171](https://github.com/47degrees/memeid/pull/171) ([scala-steward](https://github.com/scala-steward))

**Closed issues:**

- Create memeid microsite [\#101](https://github.com/47degrees/memeid/issues/101)

**Merged pull requests:**

- Update http4s-dsl to 0.21.2 [\#150](https://github.com/47degrees/memeid/pull/150) ([scala-steward](https://github.com/scala-steward))
- Update sbt-mdoc to 2.1.3 [\#149](https://github.com/47degrees/memeid/pull/149) ([scala-steward](https://github.com/scala-steward))
- Update discipline-specs2 to 1.1.0 [\#148](https://github.com/47degrees/memeid/pull/148) ([scala-steward](https://github.com/scala-steward))
- Update sbt-fix to 0.4.0 [\#144](https://github.com/47degrees/memeid/pull/144) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Create memeid microsite [\#142](https://github.com/47degrees/memeid/pull/142) ([gutiory](https://github.com/gutiory))
- Update specs2-cats, specs2-scalacheck to 4.9.2 [\#141](https://github.com/47degrees/memeid/pull/141) ([scala-steward](https://github.com/scala-steward))
- Update sbt-github-header, sbt-github-mdoc to 0.6.0 [\#140](https://github.com/47degrees/memeid/pull/140) ([scala-steward](https://github.com/scala-steward))
- Update specs2-cats, specs2-scalacheck to 4.9.1 [\#139](https://github.com/47degrees/memeid/pull/139) ([scala-steward](https://github.com/scala-steward))
- Update specs2-cats, specs2-scalacheck to 4.9.0 [\#138](https://github.com/47degrees/memeid/pull/138) ([scala-steward](https://github.com/scala-steward))

## [v0.1](https://github.com/47degrees/memeid/tree/v0.1) (2020-02-27)

[Full Changelog](https://github.com/47degrees/memeid/compare/c0825e89fa9430e420b533a1e37e6ae78b02ee96...v0.1)

🚀 **Features**

- Make `memeid-cats` not dependent on `memeid-scala` [\#91](https://github.com/47degrees/memeid/issues/91)
- Compare by fields when two UUIDs are the same version [\#78](https://github.com/47degrees/memeid/issues/78)
- Use a diferent top-level namespace for JVM lang integrations [\#75](https://github.com/47degrees/memeid/issues/75)
- Extract UUID construction to a Java-only library [\#69](https://github.com/47degrees/memeid/issues/69)
- Make implementation not dependent on cats-effect [\#41](https://github.com/47degrees/memeid/issues/41)
- Auto-draft releases and label new PRs [\#129](https://github.com/47degrees/memeid/pull/129) ([alejandrohdezma](https://github.com/alejandrohdezma))

📘 **Documentation**

- Scaladoc in comments [\#44](https://github.com/47degrees/memeid/issues/44)
- Readme that serves as docs [\#40](https://github.com/47degrees/memeid/issues/40)

**Closed issues:**

- Support Scala 2.12 & 2.13 [\#115](https://github.com/47degrees/memeid/issues/115)
- Remove dependency with circe `java.util.UUID` typeclasses [\#109](https://github.com/47degrees/memeid/issues/109)
- Extract Arbitrary instances to its own module [\#106](https://github.com/47degrees/memeid/issues/106)
- Memeid guideline [\#98](https://github.com/47degrees/memeid/issues/98)
- Release  [\#54](https://github.com/47degrees/memeid/issues/54)
- Typeclass for node information [\#26](https://github.com/47degrees/memeid/issues/26)
- Macro constructor [\#24](https://github.com/47degrees/memeid/issues/24)
- Bitwise op primitives [\#22](https://github.com/47degrees/memeid/issues/22)
- Cats typeclass instances for UUID [\#14](https://github.com/47degrees/memeid/issues/14)
- Circe integration [\#13](https://github.com/47degrees/memeid/issues/13)
- Doobie integration [\#12](https://github.com/47degrees/memeid/issues/12)
- Http4s integration [\#11](https://github.com/47degrees/memeid/issues/11)
- Constructor for creating v5 \(namespaced, SHA1 hash\) UUIDs [\#9](https://github.com/47degrees/memeid/issues/9)
- Constructor for creating v3 \(namespaced, MD5 hash\) UUIDs [\#8](https://github.com/47degrees/memeid/issues/8)
- Constructor for generating SQUUIDs \(random, time-based\) [\#7](https://github.com/47degrees/memeid/issues/7)
- Constructor for generating a v4 \(random\) UUID [\#6](https://github.com/47degrees/memeid/issues/6)
- Constructor for generating v1 \(time-based\) UUIDs [\#5](https://github.com/47degrees/memeid/issues/5)
- Functions for obatining the time components of UUIDs [\#4](https://github.com/47degrees/memeid/issues/4)
- Functions for obtaining the most and least significant bits of a UUID [\#3](https://github.com/47degrees/memeid/issues/3)
- Function for obtaining UUID version [\#2](https://github.com/47degrees/memeid/issues/2)
- Function for obtaining UUID variant [\#1](https://github.com/47degrees/memeid/issues/1)

**Merged pull requests:**

- Auto merge all scala-steward's PRs [\#127](https://github.com/47degrees/memeid/pull/127) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Range versions should not be updated by scala steward [\#126](https://github.com/47degrees/memeid/pull/126) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add descriptions for all projects [\#124](https://github.com/47degrees/memeid/pull/124) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add plugin for removing test dependencies from POM [\#123](https://github.com/47degrees/memeid/pull/123) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Use range provided dependencies for integrations [\#122](https://github.com/47degrees/memeid/pull/122) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Readme header [\#120](https://github.com/47degrees/memeid/pull/120) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Cross publish to both Scala 2.12 and 2.13 [\#118](https://github.com/47degrees/memeid/pull/118) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Remove `UUID` deps from circe and add `memeid4s-scalacheck` project [\#113](https://github.com/47degrees/memeid/pull/113) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Move docs to `memeid-docs` to fix name in docs [\#111](https://github.com/47degrees/memeid/pull/111) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Make improvements to tests [\#105](https://github.com/47degrees/memeid/pull/105) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Memeid design assets [\#102](https://github.com/47degrees/memeid/pull/102) ([israelperezglez](https://github.com/israelperezglez))
- Avoid exceptions on `UUID` constructors [\#100](https://github.com/47degrees/memeid/pull/100) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Some docs fixes that prevent releasing [\#99](https://github.com/47degrees/memeid/pull/99) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Split namespaces for Java & Scala libraries [\#96](https://github.com/47degrees/memeid/pull/96) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Enabling Github Actions [\#93](https://github.com/47degrees/memeid/pull/93) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Move V1 constructor to Java [\#90](https://github.com/47degrees/memeid/pull/90) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Update cats-effect to 2.1.1 [\#89](https://github.com/47degrees/memeid/pull/89) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Improve comparison function to compare by fields and short-circuit on… [\#79](https://github.com/47degrees/memeid/pull/79) ([purrgrammer](https://github.com/purrgrammer))
- Merge pull request \#77 from 47deg/move-v3-v4-v5-constructors-to-j… [\#77](https://github.com/47degrees/memeid/pull/77) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Make `memeid-literal` depend only on Java module [\#76](https://github.com/47degrees/memeid/pull/76) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Move UUID hierarchy to Java [\#74](https://github.com/47degrees/memeid/pull/74) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Create `memeid-java` project and move `Bits` implementation [\#72](https://github.com/47degrees/memeid/pull/72) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Re-organize project and update docs on Travis [\#71](https://github.com/47degrees/memeid/pull/71) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Functions for obatining the time components of UUIDs [\#68](https://github.com/47degrees/memeid/pull/68) ([gutiory](https://github.com/gutiory))
- Docs [\#67](https://github.com/47degrees/memeid/pull/67) ([purrgrammer](https://github.com/purrgrammer))
- Update `sbt-header` to version `5.4.0` [\#65](https://github.com/47degrees/memeid/pull/65) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add support for bloop [\#61](https://github.com/47degrees/memeid/pull/61) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Update sbt fix [\#59](https://github.com/47degrees/memeid/pull/59) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Enable Mergify [\#58](https://github.com/47degrees/memeid/pull/58) ([alejandrohdezma](https://github.com/alejandrohdezma))
- May the F be with you [\#55](https://github.com/47degrees/memeid/pull/55) ([alejandrohdezma](https://github.com/alejandrohdezma))
- 44 scaladoc in comments [\#53](https://github.com/47degrees/memeid/pull/53) ([gutiory](https://github.com/gutiory))
- Add license headers to every file [\#52](https://github.com/47degrees/memeid/pull/52) ([alejandrohdezma](https://github.com/alejandrohdezma))
- Add support for http4s [\#51](https://github.com/47degrees/memeid/pull/51) ([alejandrohdezma](https://github.com/alejandrohdezma))



\* *This Changelog was automatically generated by [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator)*
