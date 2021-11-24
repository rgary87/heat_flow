package fr.rgary.heatflow;

import static fr.rgary.heatflow.DirectionalVectorEnum.*;
import static fr.rgary.heatflow.base.Constant.BASE_TEMPERATURE;
import static fr.rgary.heatflow.base.Constant.GRID_SIZE_HEIGHT;
import static fr.rgary.heatflow.base.Constant.GRID_SIZE_WIDTH;

public class HeatGrid {
    public HeatPoint[][] grid;

    public HeatGrid() {
        this.grid = new HeatPoint[GRID_SIZE_HEIGHT][GRID_SIZE_WIDTH];
        for (int i = 0; i < GRID_SIZE_HEIGHT; i++) {
            for (int j = 0; j < GRID_SIZE_WIDTH; j++) {
                if (i == 0) {
                    this.grid[i][j] = new HeatPoint(i, j, BASE_TEMPERATURE);
                } else {
                    this.grid[i][j] = new HeatPoint(i, j, 0);
                }
            }
        }
        for (int i = 0; i < GRID_SIZE_HEIGHT; i++) {
            for (int j = 0; j < GRID_SIZE_WIDTH; j++) {
                //LEFTs
                if (i > 0 && j > 0) {
                    this.grid[i][j].setNeighbor(UP_LEFT, this.grid[i + Math.round(UP_LEFT.getVector().x)][j + Math.round(UP_LEFT.getVector().y)]);
                }
                if (j > 0) {
                    this.grid[i][j].setNeighbor(LEFT, this.grid[i + Math.round(LEFT.getVector().x)][j + Math.round(LEFT.getVector().y)]);
                }
                if (i + 1 < GRID_SIZE_HEIGHT && j > 0) {
                    this.grid[i][j].setNeighbor(BOTTOM_LEFT, this.grid[i + Math.round(BOTTOM_LEFT.getVector().x)][j + Math.round(BOTTOM_LEFT.getVector().y)]);
                }
                //RIGHTs
                if (i > 0 && j + 1 < GRID_SIZE_WIDTH) {
                    this.grid[i][j].setNeighbor(UP_RIGHT, this.grid[i + Math.round(UP_RIGHT.getVector().x)][j + Math.round(UP_RIGHT.getVector().y)]);
                }
                if (j + 1 < GRID_SIZE_WIDTH) {
                    this.grid[i][j].setNeighbor(RIGHT, this.grid[i + Math.round(RIGHT.getVector().x)][j + Math.round(RIGHT.getVector().y)]);
                }
                if (i + 1 < GRID_SIZE_HEIGHT && j + 1 < GRID_SIZE_WIDTH) {
                    this.grid[i][j].setNeighbor(BOTTOM_RIGHT, this.grid[i + Math.round(BOTTOM_RIGHT.getVector().x)][j + Math.round(BOTTOM_RIGHT.getVector().y)]);
                }
                //ELSEs
                if (i > 0) {
                    this.grid[i][j].setNeighbor(UP, this.grid[i + Math.round(UP.getVector().x)][j + Math.round(UP.getVector().y)]);
                }
                if (i + 1 < GRID_SIZE_HEIGHT) {
                    this.grid[i][j].setNeighbor(BOTTOM, this.grid[i + Math.round(BOTTOM.getVector().x)][j + Math.round(BOTTOM.getVector().y)]);
                }
            }
        }
    }

    public void moveHeat() {
        for (int i = 0; i < GRID_SIZE_HEIGHT; i++) {
            for (int j = 0; j < GRID_SIZE_WIDTH; j++) {
                this.grid[i][j].getTempDelta();
            }
        }
        for (int i = 0; i < GRID_SIZE_HEIGHT; i++) {
            for (int j = 0; j < GRID_SIZE_WIDTH; j++) {
                this.grid[i][j].applyTempDelta();
            }
        }
    }

}
