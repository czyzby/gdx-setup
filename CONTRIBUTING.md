Don't be afraid to create issues with requests and questions! It's OK to create an issue if:

- Some extension version is outdated.
- We're too lazy to update to the latest LibGDX.
- A (somewhat stable) third-party extension is missing.
- You'd like to propose a new project template, but you don't want to bother with pull requests.
- You've found a bug or just got a general question.

All Kotlin sources should be formatted with the default Kotlin IntelliJ formatter.

### Adding a new third-party extension

Open `thirdPartyExtensions.kt`. Create a new class implementing `ThirdPartyExtension`. Annotate it with `@Extension` - it will be automatically scanned for and initiated, you don't have to register it anywhere else in Kotlin sources. Choose a unique ID, default version and contact URL. Override `initiateDependencies` to include libraries and GWT inherits of the extensions. Add `yourLibId` line in `nls.properties` (displayed name of the library) and `yourLibIdTip` in all other NLS files (tooltips with library description).

### Adding a new JVM language support

Open `langs` package. Create a new file with a new class implementing `Language`. Annotate it with `@JvmLanguage`. Choose a unique language ID and proposed runtime library version. Override `initiate` method to include language support in the project. Add `langId` line (official language name) and `langIdUrl` (official language website) in `nls.properties`.

### Adding a new sources template

Open `templates` package. Create a new file in `unofficial` package with a new class implementing `Template`. Annotate it with `@ProjectTemplate`. Choose a unique ID. Implement `getApplicationListenerContent` - provide source code of `ApplicationListener` implementation. If you need to add additional files or modify the project structure itself, override `apply` method. Add `templateId` line (default template name) in `nls.properties` and `templateIdTip` in every other NLS file (template description).

### Providing a new translation

Copy `nls.properties` file. Add ID of your language to its name, for example: `nls_en.properties`. Delete all lines up to `### THESE LINES SHOULD BE TRANSLATED:`. Translate all other bundle lines. If you think any additional lines from the `nls.properties` should be translated as well (like template names), copy and translate them in your new file: they will be overridden. Find `availableLocales` variable in `configuration.kt` and add your language ID. Add line matching your language ID to `nls.properties` with the language's native name. Note that special characters should be escaped - for example, `ó` should be entered as `\u00F3` to ensure that it works without any problems on every platform. Eclipse IDE should automatically convert these characters as you type them in. IntelliJ users can check "Transparent native-to-ascii conversion" in Settings and use integrated I18N bundle editor to achieve this.
