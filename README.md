# Message of the Day (MOTD) Plugin

A robust, lightweight **Hytale** server plugin designed to greet players with customizable titles and subtitles upon joining. This plugin features a "Code-First" configuration system that automatically generates and manages its own directory.

---

## 🚀 Features

* **Custom Greetings:** Display a primary and secondary message using the Hytale `EventTitleUtil`.
* **Dynamic Placeholders:** Supports `%player%` in both title and subtitle to automatically insert the player's display name.
* **Auto-Generating Config:** Generates a dedicated configuration folder and file in the directory where the JAR is located.
* **Commented JSON Support:** The `config.json` uses `Strictness.LENIENT` parsing, allowing you to include `//` comments for instructions without breaking the plugin.
* **Live Reloading:** Update your messages in real-time without restarting the server. **(Coming Soon)**

---

## 📂 Installation

1.  **Download/Build:** Grab the `MOTD-Plugin.jar` from your build folder.
2.  **Deploy:** Place the JAR file into your Hytale server's `mods` or `plugins` folder.
3.  **Run:** Start your server once to generate the configuration folder.
4.  **Configure:** Navigate to the newly created folder: `Message of the Day Config/config.json`.

---

## ⚙️ Configuration

The plugin generates a `config.json` with the following structure:

```json
// INSTRUCTIONS:
// Use %player% to insert the player's name.

// EXAMPLES:
// "Happy Spooky Season, %player%!"
// "Merry Christmas, %player%!"

{
  "primaryMessage": "Welcome %player%",
  "secondaryMessage": "Glad you are here."
}
