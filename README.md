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
- Gradle wrapper and running Gradle tasks after generation is entirely optional. This can significantly speed up the generation process and limit the project size if you'd prefer to use a globally installed Gradle application.

Currently missing:
- iOS platform support. Delayed until it becomes stable (the whole RoboVM to MOE transition thing).
- `TextArea` widget is a bit clunky when it comes to displaying generation data. It wasn't really prepared to be embedded in a `ScrollPane`. We're working on it.

### Running the application

You can download the latest runnable jar [here](https://dl.kotcrab.com/libgdx/gdx-setup-latest.jar). Stable application versions are uploaded to the [releases section](https://github.com/czyzby/gdx-setup/releases).

#### Running from sources

```
git clone https://github.com/czyzby/gdx-setup.git
cd gdx-setup
gradle run
```

*Pro tip*: check the `Generate skin assets` option in `Advanced` tab and `Desktop` in `Platforms` tab. Enter `pack desktop:run --daemon` Gradle tasks in `Advanced` tab. Delete project directory with the trash icon after each run. Now you can easily test all templates one by one.

## Contributing

You can start with creating an issue: if that's something trivial like adding a new extension or updating some version, it will most likely be quickly resolved. If you want to modify the sources and pull a request, check out [contribution guide](CONTRIBUTING.md).
