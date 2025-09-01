# Accelerated Training Environment

Running full Minecraft at 1000× normal speed is not practical, but you can significantly speed up training by using a simplified headless environment. We suggest using [Malmo](https://github.com/microsoft/malmo) or the [MineRL](https://github.com/minerllabs/minerl) simulator. These let you step the world without rendering and bypass many client delays.

1. Use the simulator to collect state/action data and train your model locally (e.g., with PyTorch or DL4J).
2. Keep the ML model as a separate component so `altoclef` can load it for inference when running the real game.
3. Adjust tick rate or run multiple simulators in parallel to gather more experience quickly.

The real game can still run with the existing Java bot code, while the model improves via faster simulated training.
