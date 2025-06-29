package adris.altoclef.tasks.resources;

import adris.altoclef.AltoClef;
import adris.altoclef.tasks.construction.DestroyBlockTask;
import adris.altoclef.tasks.movement.DefaultGoToDimensionTask;
import adris.altoclef.tasks.movement.TimeoutWanderTask;
import adris.altoclef.ai.SimpleQLearning;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.util.Dimension;
import adris.altoclef.util.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Destroys all blaze spawners found in the world.
 */
public class DestroyBlazeSpawnersTask extends Task {

    private final Set<BlockPos> _destroyed = new HashSet<>();
    private BlockPos _current = null;
    private int _lastState = 0;
    private final SimpleQLearning _agent = new SimpleQLearning(2, 0.1, 0.9, 0.2);

    @Override
    protected void onStart(AltoClef mod) {
        mod.getBlockTracker().trackBlock(Blocks.SPAWNER);
    }

    @Override
    protected Task onTick(AltoClef mod) {
        if (WorldHelper.getCurrentDimension() != Dimension.NETHER) {
            setDebugState("Going to nether");
            return new DefaultGoToDimensionTask(Dimension.NETHER);
        }

        if (_current != null) {
            if (WorldHelper.isAir(mod, _current)) {
                _destroyed.add(_current);
                _agent.update(_lastState, 0, 100, 0);
                _current = null;
            } else {
                setDebugState("Destroying spawner at " + _current.toShortString());
                return new DestroyBlockTask(_current);
            }
        }

        List<BlockPos> spawners = mod.getBlockTracker().getKnownLocations(Blocks.SPAWNER);
        for (BlockPos pos : spawners) {
            if (_destroyed.contains(pos)) continue;
            if (isBlazeSpawner(mod, pos)) {
                int state = getState(mod, pos);
                int action = _agent.chooseAction(state);
                if (action == 0) {
                    _current = pos;
                    _lastState = state;
                    return new DestroyBlockTask(pos);
                } else {
                    _destroyed.add(pos);
                    _agent.update(state, 1, -10, 0);
                }
            }
        }

        _agent.update(_lastState, 0, -0.1, 0);
        setDebugState("Searching for blaze spawner");
        return new TimeoutWanderTask();
    }

    private boolean isBlazeSpawner(AltoClef mod, BlockPos pos) {
        if (!mod.getChunkTracker().isChunkLoaded(pos)) return false;
        return WorldHelper.getSpawnerEntity(mod, pos) instanceof BlazeEntity;
    }

    private int getState(AltoClef mod, BlockPos pos) {
        double distSq = pos.getSquaredDistance(mod.getPlayer().getPos());
        int distBucket = distSq < 100 ? 0 : distSq < 400 ? 1 : 2;
        int blazeNearby = mod.getEntityTracker().getTrackedEntities(BlazeEntity.class)
                .stream().anyMatch(e -> e.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64) ? 1 : 0;
        return distBucket * 2 + blazeNearby;
    }

    @Override
    public boolean isFinished(AltoClef mod) {
        for (BlockPos pos : mod.getBlockTracker().getKnownLocations(Blocks.SPAWNER)) {
            if (!_destroyed.contains(pos) && isBlazeSpawner(mod, pos)) {
                return false;
            }
        }
        return _current == null;
    }

    @Override
    protected void onStop(AltoClef mod, Task interruptTask) {
        mod.getBlockTracker().stopTracking(Blocks.SPAWNER);
    }

    @Override
    protected boolean isEqual(Task other) {
        return other instanceof DestroyBlazeSpawnersTask;
    }

    @Override
    protected String toDebugString() {
        return "Destroy Blaze Spawners";
    }
}

