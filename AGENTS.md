# エージェント向けリポジトリガイド

## プロジェクト概要

- このリポジトリは、Web サイトのアイコンを取得する JVM・Android 向けの Kotlin ライブラリです。Android アプリではありません。
- `touchicon/` は本体です。HTML と Web App Manifest の解析、ドメイン直下のアイコンの探索、`HttpURLConnection` を使う HTTP アダプターを提供します。
- `touchicon-http/okhttp/` は任意で利用できる OkHttp アダプターです。本体モジュールの本番用依存関係に OkHttp を追加しないでください。
- `build-logic/` には両モジュールで使う Gradle の共通プラグインがあります。依存ライブラリのバージョンは `gradle/libs.versions.toml` で管理します。
- `README.md` に公開 API と動作の説明があります。`docs/dokka/` には生成された API ドキュメントがあります。

## 開発時の方針

- リポジトリのルートから Gradle Wrapper を使ってください。CI は JDK 21 を使い、ライブラリは JVM 11 を対象としています。
- `.editorconfig` に従ってください。文字コードは UTF-8、改行は LF、インデントはスペース 4 個とし、ファイル末尾に改行を入れます。Kotlin の記述と著作権ヘッダーは既存のソース・テストに合わせてください。
- 明示的に破壊的変更が求められていない限り、公開 API の互換性を維持してください。公開 API の動作や使い方を変える場合は KDoc と `README.md` も更新してください。
- アイコン情報の取得と画像データのダウンロードを区別してください。ドメイン直下の探索では HTTP リクエストが発生するため、理由なくリクエスト数や探索順を変えないでください。
- 本体は OkHttp なしでも使える状態を保ってください。`HttpClientAdapter` や `HttpResponse` を変更した場合は、標準実装と OkHttp 実装の両方を確認してください。
- 動作を変更する場合は、該当モジュールの `src/test/kotlin/` に焦点を絞ったテストを追加・更新してください。既存のテストでは JUnit 4、Truth、MockK、MockWebServer を使っています。

## 検証

- ライブラリの動作を変更した場合は `./gradlew :touchicon:test :touchicon-http:okhttp:test` を実行してください。
- Kotlin または Gradle スクリプトを変更した場合は `./gradlew ktlint` を実行してください。このタスクは `isIgnoreExitValue = true` なので、Gradle が成功と表示しても出力に違反がないか確認してください。
- 依存関係を変更した場合は `./gradlew dependencyGuard` を実行してください。チェックイン済みのベースラインは、依存関係の変更が意図的な場合にのみ更新してください。
- CI は JDK 21 で `./gradlew :touchicon:koverXmlReport --warning-mode all --no-configuration-cache` を実行します。
- 現在の環境でコマンドを実行できない場合は、成功したと述べずにその制約を報告してください。
