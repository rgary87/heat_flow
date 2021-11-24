package fr.rgary.heatflow;

import fr.rgary.heatflow.base.Constant;
import fr.rgary.heatflow.display.Color;
import fr.rgary.heatflow.trigonometry.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static fr.rgary.heatflow.base.Constant.MAX_HEAT_FLOW;

public class HeatPoint {
    public int x = 0;
    public int y = 0;
    public float temperature;
    public float temperatureDelta = 0;
    public Vector direction;
    public Map<DirectionalVectorEnum, HeatPoint> neighbours = new EnumMap<>(DirectionalVectorEnum.class);

    public HeatPoint() {
        this.x = 0;
        this.y = 0;
        this.temperature = 0;
        this.direction = new Vector();
    }

    public HeatPoint(int x, int y, float temperature) {
        this.x = x;
        this.y = y;
        this.temperature = temperature;
    }

    public Color getColor() {
        if (temperature >= 300) {
            return new Color(1f, 1f, 1f);
        }
        float r = (temperature % 100) / 100f;
        float g = 0;
        float b = 0;
        if (temperature > 200) {
            b = 1;
        }
        if (temperature > 100) {
            g = 1;
        }
        return new Color(r, g, b);
    }

    public void setNeighbours(Map<DirectionalVectorEnum, HeatPoint> neighbours) {
        this.neighbours = neighbours;
    }

    public void setNeighbor(DirectionalVectorEnum direction, HeatPoint neighbor) {
        this.neighbours.put(direction, neighbor);
    }

    public void getTempDelta() {
        float tmp = 0;
        if (x + 1 == Constant.GRID_SIZE_WIDTH || y + 1 == Constant.GRID_SIZE_HEIGHT) {
            return;
        }
        int cnt = 0;
        for (HeatPoint heatPoint : neighbours.values()) {
            if (heatPoint == null || heatPoint.temperature >= this.temperature) continue;
            cnt++;
            float rand = 0.5f + (float) Constant.RANDOM.nextDouble() * (0.5f);
            tmp += (heatPoint.temperature - this.temperature);
            if (tmp < -MAX_HEAT_FLOW) { // tmp is negative
                tmp = rand * -MAX_HEAT_FLOW;
            }
        }
        tmp = Math.min(temperature / 8, Math.abs(tmp)) * -1;
        if (cnt == 0) return;
        this.selfTempDelta(tmp); // tmp is negative to remove temp from heat point
        tmp = -tmp;
        float part =  tmp / cnt; // part is positive to add temp to heat points around
        for (HeatPoint heatPoint :  neighbours.values()) {
            if (heatPoint == null || heatPoint.temperature >= this.temperature) continue;
            heatPoint.addTempDelta(part);
        }
    }

    public float selfTempDelta(float delta) {
        if (delta > 0) {
            LOGGER.info("STAHP");
        }
        temperatureDelta += delta;
        return 0;
    }

    public float addTempDelta(float delta) {

        temperatureDelta += delta;
        return 0;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HeatPoint.class);
    public void applyTempDelta() {
        if (temperature + temperatureDelta < 0.0001) {
            temperature = 0;
            temperatureDelta = 0;
            return;
        }
        if (temperatureDelta < 0 && temperature < Math.abs(temperatureDelta)) {
            LOGGER.info("{}|{} -> {} -> delta {}", x, y, temperature, temperatureDelta);
        }
        temperature += temperatureDelta;
        temperatureDelta = 0;
    }

}
