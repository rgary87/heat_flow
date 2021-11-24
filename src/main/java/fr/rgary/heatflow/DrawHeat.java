package fr.rgary.heatflow;

import fr.rgary.heatflow.base.Constant;
import fr.rgary.heatflow.display.Color;
import fr.rgary.heatflow.display.Draw;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fr.rgary.heatflow.base.Constant.DISPLAY_HEIGHT_SPACING;
import static fr.rgary.heatflow.base.Constant.DISPLAY_WIDTH_SPACING;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class DrawHeat extends Draw {

    private static final Logger LOGGER = LoggerFactory.getLogger(DrawHeat.class);

    static final List<Integer> logMyInfo = new ArrayList<>(Arrays.asList(0, 10, 50, 100, 150));
//    static final List<Integer> logMyInfo = new ArrayList<>(Arrays.asList(0));

    public static void DrawHeatGrid(HeatGrid grid) {
        for (int i = 0; i < Constant.GRID_SIZE_HEIGHT - 1; i++) {
            for (int j = 0; j < Constant.GRID_SIZE_WIDTH - 1; j++) {
//                if ((logMyInfo.contains(i) && i == j) || (j == 240 && i == 10)) {
//                    LOGGER.info("Grid[{}][{}].temperature = {}", i, j, grid.grid[i][j].temperature);
//                    LOGGER.info("Color's : {}|{}|{}", grid.grid[i][j].getColor().R, grid.grid[i][j].getColor().G, grid.grid[i][j].getColor().B);
//                }
                glBegin(GL11.GL_QUADS);
                //Top left
                customGlColor3d(grid.grid[i][j].getColor());
                glVertex2f(i * DISPLAY_WIDTH_SPACING, j * DISPLAY_HEIGHT_SPACING);
                //Top Right
                customGlColor3d(grid.grid[i][j + 1].getColor());
                glVertex2f(i * DISPLAY_WIDTH_SPACING, (j + 1) * DISPLAY_HEIGHT_SPACING);
                //Bottom Right
                customGlColor3d(grid.grid[i + 1][j + 1].getColor());
                glVertex2f((i + 1) * DISPLAY_WIDTH_SPACING, (j + 1) * DISPLAY_HEIGHT_SPACING);
                //Bottom Left
                customGlColor3d(grid.grid[i + 1][j].getColor());
                glVertex2f((i + 1) * DISPLAY_WIDTH_SPACING, j * DISPLAY_HEIGHT_SPACING);
//                LOGGER.info("Square is:");
//                LOGGER.info("[{}:{}] [{}:{}]", i * widthSpacing, j * heightSpacing, i * widthSpacing, (j + 1) * heightSpacing);
//                LOGGER.info("[{}:{}] [{}:{}]", (i + 1) * widthSpacing, (j + 1) * heightSpacing, (i + 1) * widthSpacing, j * heightSpacing);
                glEnd();
            }
        }
    }

    public static void customGlColor3d(Color color) {
//        LOGGER.info("Color's : {}|{}|{}", color.R, color.G, color.B);
        glColor3d(color.R, color.G, color.B);
    }

}
