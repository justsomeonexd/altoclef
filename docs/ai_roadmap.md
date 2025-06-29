# AI-Based Bot Roadmap

This roadmap outlines how to run multiple AltoClef bots with simple
reinforcement learning logic.

1. **Train the Agents**
   - The project includes a tiny Q-learning helper located in
     `src/main/java/adris/altoclef/ai/SimpleQLearning.java`.
   - The `DestroyBlazeSpawnersTask` uses this helper to decide which spawner to
     destroy next. Over time it learns the most efficient strategy.
   - Extend this pattern to other tasks (such as beating the game) by creating
     planners that evaluate rewards for various actions.

2. **Run Multiple Bots**
   - Use two separate Minecraft accounts and launch two clients:
     ```bash
     ./gradlew runClient --args="--username <ACCOUNT1>"
     ./gradlew runClient --args="--username <ACCOUNT2>"
     ```
   - Assign `@gamer` to the first bot and `@destroy_spawners` to the second.

3. **Expand the Learning**
   - Persist the Q-tables between sessions to keep improving.
   - Add more state features (gear level, health, etc.) for smarter decisions.

This approach allows the bots to improve at their tasks the more they play.
