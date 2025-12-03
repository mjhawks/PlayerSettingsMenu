# Player Preferences Menu (Minecraft Plugin)

A lightweight Paper/Spigot plugin that gives every player access to a personal preferences menu.  
Players can toggle PvP on/off and choose whether other players are visible to them.  
All settings are stored in each player's PersistentDataContainer, ensuring preferences remain saved across disconnects and server restarts.

---

## 🌟 Features

- **PvP Toggle**  
  Players can individually enable or disable their ability to participate in PvP combat.

- **Hide/Show Other Players**  
  Players can make all other players invisible or visible on demand.

- **Persistent Saving**  
  All preferences are saved per-player using the Bukkit `PersistentDataContainer`, so they automatically reload when the player rejoins or the server restarts.

- **Menu-Based UI**  
  Clean, inventory-based menu accessible by all players.

---

## 📦 Commands

| Command | Description |
|---------|-------------|
| `/menu` | Opens the player’s personal settings menu |


---

## ⚙️ How It Works

- When a player uses the menu, their selections are immediately stored in their `PersistentDataContainer`.
- PvP enforcement is handled by monitoring damage events and checking each player's saved preference.
- Preferences are automatically applied on join.

---

## 🛠️ Installation

1. Download the plugin JAR.
2. Place it in your server's `plugins` folder.
3. Restart the server.
4. Players can now use `/menu`.

---

## 📁 Configuration

Currently, all settings are per-player and do not require a config file.  
If future versions add customization options, they will appear here.

---

## 🧩 Compatibility

- **Minecraft:** 1.20+
- **Server:** Paper / Spigot
- Fully compatible with other plugins unless they override visibility or PvP events.

---

## 📜 License

GPLv3 License 

---

## 🤝 Contributions

Pull requests and feature suggestions are welcome!  
Feel free to open an issue for bugs, ideas, or improvements.

