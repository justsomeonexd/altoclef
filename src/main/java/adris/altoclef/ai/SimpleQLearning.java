package adris.altoclef.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Extremely small Q-learning helper used for decision making.
 */
public class SimpleQLearning {
    private final int actionCount;
    private final Map<Long, Double> qValues = new HashMap<>();
    private final Random random = new Random();
    private final double alpha;
    private final double gamma;
    private double epsilon;

    public SimpleQLearning(int actionCount, double alpha, double gamma, double epsilon) {
        this.actionCount = actionCount;
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
    }

    private long key(int state, int action) {
        return (((long) state) << 32) | (action & 0xffffffffL);
    }

    public int chooseAction(int state) {
        if (random.nextDouble() < epsilon) {
            return random.nextInt(actionCount);
        }
        int best = 0;
        double bestVal = Double.NEGATIVE_INFINITY;
        for (int a = 0; a < actionCount; a++) {
            double v = getQ(state, a);
            if (v > bestVal) {
                bestVal = v;
                best = a;
            }
        }
        return best;
    }

    public void update(int state, int action, double reward, int nextState) {
        double oldVal = getQ(state, action);
        double nextMax = Double.NEGATIVE_INFINITY;
        for (int a = 0; a < actionCount; a++) {
            nextMax = Math.max(nextMax, getQ(nextState, a));
        }
        double newVal = oldVal + alpha * (reward + gamma * nextMax - oldVal);
        qValues.put(key(state, action), newVal);
    }

    private double getQ(int state, int action) {
        return qValues.getOrDefault(key(state, action), 0.0);
    }
}
