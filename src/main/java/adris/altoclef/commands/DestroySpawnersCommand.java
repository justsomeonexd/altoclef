package adris.altoclef.commands;

import adris.altoclef.AltoClef;
import adris.altoclef.commandsystem.ArgParser;
import adris.altoclef.commandsystem.Command;
import adris.altoclef.tasks.resources.DestroyBlazeSpawnersTask;

/**
 * Command that makes the bot search for and destroy blaze spawners.
 */
public class DestroySpawnersCommand extends Command {

    public DestroySpawnersCommand() {
        super("destroy_spawners", "Destroy all blaze spawners in the nether");
    }

    @Override
    protected void call(AltoClef mod, ArgParser parser) {
        mod.runUserTask(new DestroyBlazeSpawnersTask(), this::finish);
    }
}
