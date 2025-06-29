# Multi-Bot Setup

This document describes how to run multiple AltoClef bots on the same server.

1. **Prepare two Minecraft accounts.** Each instance must authenticate with a unique account.
2. **Build the project:**
   ```bash
   ./gradlew build -x test
   ```
3. **Launch each client in a separate terminal:**
   ```bash
   ./gradlew runClient --args="--username <ACCOUNT1>"
   ./gradlew runClient --args="--username <ACCOUNT2>"
   ```
4. **Assign commands to each bot.** For example, tell one bot to beat the game:
   ```
   @gamer
   ```
   And the other to destroy blaze spawners:
   ```
   @destroy_spawners
   ```

Bots communicate via the normal chat command system. See `usage.md` for the full list of commands.
