# Manhunt Plugin
A little minecraft manhunt plugin with translations I made for a friend

## Usage
You can add runners by using `/manhunt add runner <name>` and add hunters using `/manhunt add hunter <name>`<br>
You can also remove them using the `remove` subcommand, like `/manhunt remove <runner | hunter> <name>`<br>
To start the hunt, use `/manhunt start`<br>

When the hunt starts, everybody is given full saturation, food level and health. Time is set to 0 and the hunters get the tracking compass.

**Using the tracking compass**
- If 1 runner
  - Pressing right-click while the compass is in your hand will make it point to the runners location
- If more than 1 runner
  - Pressing right-click opens a little GUI with the runners heads. You will have to choose a runner, the compass will point at the runner you chose.

## Configuration
Currently, there is only one field in the config file.<br>
- `language`:
  - Takes a language code as a string, if the language exists in the langs/ directory in the plugins config directory or is present in the plugins jar, it will take translations from there, otherwise it will automatically take translations from `en_us`<br>
  - Current translations: `en_us`: English (United States), `ru_ru`: Russian (Russia), `ru_pr`: Pre-reform russian (Russian Empire)

## Permissions
- `manhunt.add`: Add players to the list of hunters/runners
- `manhunt.remove`: Remove players from the list of hunters/runners
- `manhunt.start`: Start the manhunt
- `manhunt.reload`: Reload the manhunt config
