package fr.rgary.heatflow.display;

import fr.rgary.heatflow.trigonometry.Line;
import fr.rgary.heatflow.trigonometry.Point;
import fr.rgary.heatflow.trigonometry.Zone;
import org.ejml.simple.SimpleMatrix;
import org.lwjgl.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;

/**
 * Class Draw.
 */
public class Draw {
    private static final Logger LOGGER = LoggerFactory.getLogger(Draw.class);

    public static int GLOBAL_HORIZONTAL_DISPLACEMENT = 0;
    public static int GLOBAL_VERTICAL_DISPLACEMENT = 450;
    public static int STATIC_ELEM = 0;
    public static int MOVING_ELEM = 1;
    static Color activeCarColor = new Color(1, 1, 1);
    static Color inactiveCarColor = new Color(1, 0, 0, 1);
    static List<Line> marks = new ArrayList<>();
    public static List<Line> drawnLines = new ArrayList<>();

    public static void drawAnyText(String text, float x, float y, int movingPart) {
        Draw.drawAnyText(text, x, y, 169f, 183f, 198f, 1f, 1f, movingPart);
    }

    public static void drawAnyText(String text, float x, float y, Color color, int movingPart) {
        Draw.drawAnyText(text, x, y, (float) color.R * 255, (float) color.G * 255, (float) color.B * 255, 1f, 1f, movingPart);
    }

    public static void drawAnyText(String text, float x, float y, Color color, float scaleX, float scaleY, int movingPart) {
        Draw.drawAnyText(text, x, y, (float) color.R * 255, (float) color.G * 255, (float) color.B * 255, scaleX, scaleY, movingPart);
    }

    public static void drawLine(Line line) {
        drawLine(line, 1);
    }

    private static void drawLine(Line line, int movingPart) {
        glBegin(GL_LINES);
        glColor3d(line.C.R, line.C.G, line.C.B);
        glVertex2d(line.S.X + (GLOBAL_HORIZONTAL_DISPLACEMENT * movingPart), line.S.Y + (GLOBAL_VERTICAL_DISPLACEMENT * movingPart));
        glVertex2d(line.E.X + (GLOBAL_HORIZONTAL_DISPLACEMENT * movingPart), line.E.Y + (GLOBAL_VERTICAL_DISPLACEMENT * movingPart));
        glEnd();
    }

    private static void drawZone(Zone zone) {
        glBegin(GL_QUADS);
        glColor4d(1, 0.2, 0.6, 10);
        glVertex2d( zone.polygon.xpoints[0] - 1_000_000 + GLOBAL_HORIZONTAL_DISPLACEMENT,zone.polygon.ypoints[0] - 1_000_000 + GLOBAL_VERTICAL_DISPLACEMENT);
        glVertex2d( zone.polygon.xpoints[1] - 1_000_000 + GLOBAL_HORIZONTAL_DISPLACEMENT,zone.polygon.ypoints[1] - 1_000_000 + GLOBAL_VERTICAL_DISPLACEMENT);
        glVertex2d( zone.polygon.xpoints[2] - 1_000_000 + GLOBAL_HORIZONTAL_DISPLACEMENT,zone.polygon.ypoints[2] - 1_000_000 + GLOBAL_VERTICAL_DISPLACEMENT);
        glVertex2d( zone.polygon.xpoints[3] - 1_000_000 + GLOBAL_HORIZONTAL_DISPLACEMENT,zone.polygon.ypoints[3] - 1_000_000 + GLOBAL_VERTICAL_DISPLACEMENT);
        glEnd();
    }



    private static void drawQuad(Point cornerTopLeft, Point cornerTopRight, Point cornerBottomRight, Point cornerBottomLeft, Color color) {
        glBegin(GL_QUADS);
        glColor3d(color.R, color.G, color.B);
        glVertex2d(cornerTopLeft.X + GLOBAL_HORIZONTAL_DISPLACEMENT, cornerTopLeft.Y + GLOBAL_VERTICAL_DISPLACEMENT);
        glVertex2d(cornerTopRight.X + GLOBAL_HORIZONTAL_DISPLACEMENT, cornerTopRight.Y + GLOBAL_VERTICAL_DISPLACEMENT);
        glVertex2d(cornerBottomRight.X + GLOBAL_HORIZONTAL_DISPLACEMENT, cornerBottomRight.Y + GLOBAL_VERTICAL_DISPLACEMENT);
        glVertex2d(cornerBottomLeft.X + GLOBAL_HORIZONTAL_DISPLACEMENT, cornerBottomLeft.Y + GLOBAL_VERTICAL_DISPLACEMENT);
        glEnd();
    }

//    public static void drawInfoTexts(long prevSecFPS, Processor processor, int infoLineHeight) {
//        int lines = 1;
//        Draw.drawAnyText(String.format("%d GEN",GENERATION), 15, infoLineHeight * lines, STATIC_ELEM);
//        lines++;
//        Draw.drawAnyText(String.format("%d ACTIVE", processor.activeCarCount), 15, infoLineHeight * lines, STATIC_ELEM);
//        lines++;
//        Draw.drawAnyText(String.format("%dFPS", prevSecFPS), 15, infoLineHeight * lines, STATIC_ELEM);
//        lines++;
//        Draw.drawAnyText("VSYNC is " + (VSYNC == 0 ? "off" : "on"), 15, infoLineHeight * lines, STATIC_ELEM);
//        lines++;
//        Draw.drawAnyText("Mouse is " + (TRACK_MOUSE ? "" : "not ") + "active", 15, infoLineHeight * lines, STATIC_ELEM);
//        lines++;
//        Draw.drawAnyText("Draw level is " + Constant.DRAW_LEVEL.label, 15, infoLineHeight * lines, STATIC_ELEM);
//    }

    public static void drawAnyText(String text, float x, float y, float r, float g, float b, float scaleX, float scaleY, int movingPart) {
        ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 400);
        int firstQuads = stb_easy_font_print(0, 0, text, null, charBuffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);

        glColor3f(r / 255f, g / 255f, b / 255f); // Text color

        glPushMatrix();

        // Zoom
        glScalef(scaleX, scaleY, 1f);
        glTranslatef((x / scaleX) + (GLOBAL_HORIZONTAL_DISPLACEMENT * movingPart), (y / scaleY) + (GLOBAL_VERTICAL_DISPLACEMENT * movingPart), 0f);

        glDrawArrays(GL_QUADS, 0, firstQuads * 4);

        glPopMatrix();
        glEnd();
    }

    public static void drawCrossMark(Point center) {
        marks.add(new Line(
                new Point(center.X - 15, center.Y - 15),
                new Point(center.X + 15, center.Y + 15)
        ));
        marks.add(new Line(
                new Point(center.X - 15, center.Y + 15),
                new Point(center.X + 15, center.Y - 15)
        ));

    }

    public static Point matRotatePoint(Point center, Point point, Double rotation) {
        SimpleMatrix rotMat = getRotationMatrix(rotation);
        SimpleMatrix simpleMatrix = new SimpleMatrix(1, 2, true, new double[]{point.X - center.X, point.Y - center.Y});
        SimpleMatrix result = simpleMatrix.mult(rotMat);
        double x = result.get(0, 0) + center.X;
        double y = result.get(0, 1) + center.Y;
        Point p = new Point(
                Math.toIntExact(Math.round(result.get(0, 0) + center.X)),
                Math.toIntExact(Math.round(result.get(0, 1) + center.Y))
        );
        return p;
    }

    public static Point matRotatePointForCar(Point center, Point point, Double rotation) {
        SimpleMatrix rotMat = getRotationMatrix(rotation);
        SimpleMatrix simpleMatrix = new SimpleMatrix(1, 2, true, new double[]{point.carX - center.carX, point.carY - center.carY});
        SimpleMatrix result = simpleMatrix.mult(rotMat);
        double x = result.get(0, 0) + center.carX;
        double y = result.get(0, 1) + center.carY;
        Point p = new Point(
                result.get(0, 0) + center.carX,
                result.get(0, 1) + center.carY
        );
        return p;
    }

    public static Line matRotateLine(Point center, Line line, Double rotation) {
        SimpleMatrix rotMat = getRotationMatrix(rotation);
        SimpleMatrix point1Matrix = new SimpleMatrix(1, 2, true, new double[]{line.S.X - center.X, line.S.Y - center.Y});
        SimpleMatrix point2Matrix = new SimpleMatrix(1, 2, true, new double[]{line.E.X - center.X, line.E.Y - center.Y});
        SimpleMatrix tmp = point1Matrix.concatRows(point2Matrix);
        SimpleMatrix result = tmp.mult(rotMat);
        return new Line(new Point(
                Math.toIntExact(Math.round(result.get(0, 0) + center.X)),
                Math.toIntExact(Math.round(result.get(0, 1) + center.Y))
        ),
                new Point(
                        Math.toIntExact(Math.round(result.get(1, 0) + center.X)),
                        Math.toIntExact(Math.round(result.get(1, 1) + center.Y))
                ));
    }

    private static SimpleMatrix getRotationMatrix(Double rotation) {
        return new SimpleMatrix(2, 2, true, new double[]{Math.cos(rotation), -Math.sin(rotation), Math.sin(rotation), Math.cos(rotation)});
    }


}
