# gdx-setup

Work in progress. This is meant to be a replacement for the current `gdx-setup` application, which... well, has its flaws. The biggest of which is *Swing* usage.

Additionally to supporting most official `gdx-setup` features, you might want to consider switching because of:
- Project templates. You can choose the initial generated sources of the project - you're no longer forced to use `ApplicationAdapter` and BadLogic logo. (No offence, guys!)
- Input validation. Your project data is validated as you type it in.
- Other JVM languages support. You can choose additional languages for your project - their runtime libraries, Gradle plugins and source folders will be included.
- Assets folder is now in the root directory and is properly linked by all projects. No more missing resources for the desktop project.
- Much more settings. You have more control over the versions of software used by your application.
- More third-party extensions.
- Preferences, favorites. Basic data of your application is saved, so you don't have to fill it each time you generate a project. File chooser allows you to add your "favorite" workspace folders.
- Internationalization! The application can be translated to different languages. It's currently available in English and Polish.

Currently missing:
- Success screen. After generation, you're just sort of left with the same screen that you were initially on.
- Gradle wrapper, running Gradle tasks after generation. Gradle wrapper is meant to be optional in this `gdx-setup` - the option to add it to the generated project is planned, and will be available through advanced options tab.
- iOS platform support. Delayed until it becomes stable (the whole RoboVM to MOE transition thing).

### Running from sources

```
git clone https://github.com/czyzby/gdx-setup.git
cd gdx-setup
gradle run
```

## Contributing

You can start with creating an issue: if that's something trivial like adding a new extension or updating some version, it will most likely be quickly resolved. If you want to modify the sources and pull a request, check out [contribution guide](CONTRIBUTING.md).
