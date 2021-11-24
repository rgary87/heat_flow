package fr.rgary.heatflow.base;

import fr.rgary.heatflow.HeatGrid;

import java.util.Random;

/**
 * Class Constant.
 */
public class Constant {
    public static final Random RANDOM = new Random();
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 1200;

    public static final int GRID_SIZE_WIDTH = 100;
    public static final int GRID_SIZE_HEIGHT = 100;

    public static int VSYNC = 0;
    public static boolean PAUSE = false;

    public static float MAX_HEAT_FLOW = 100;

    public static int INTER_FRAME_DELAY = 50;

    public static float DISPLAY_WIDTH_SPACING = (float) Constant.WINDOW_WIDTH / (float) Constant.GRID_SIZE_WIDTH;
    public static float DISPLAY_HEIGHT_SPACING = (float) Constant.WINDOW_HEIGHT / (float) Constant.GRID_SIZE_HEIGHT;

    public static float BASE_TEMPERATURE = 5000f;

    public static HeatGrid HEAT_GRID = new HeatGrid();

}
