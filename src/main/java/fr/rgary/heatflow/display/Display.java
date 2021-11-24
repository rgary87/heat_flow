/*
 * Copyright (C) 2018 Eir.
 */
package fr.rgary.heatflow.display;

import fr.rgary.heatflow.DrawHeat;
import fr.rgary.heatflow.HeatGrid;
import fr.rgary.heatflow.base.Constant;
import fr.rgary.heatflow.trigonometry.Point;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.Objects;

import static fr.rgary.heatflow.base.Constant.DISPLAY_HEIGHT_SPACING;
import static fr.rgary.heatflow.base.Constant.DISPLAY_WIDTH_SPACING;
import static fr.rgary.heatflow.base.Constant.HEAT_GRID;
import static fr.rgary.heatflow.base.Constant.WINDOW_HEIGHT;
import static fr.rgary.heatflow.base.Constant.WINDOW_WIDTH;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorContentScale;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Class Display.
 */
public class Display {
    private static final Logger LOGGER = LoggerFactory.getLogger(Display.class);

    public static Font FONT;
    // The window handle
    private long window;

    public Display() {
    }

    public void run() {
        this.init();
        this.callBacks();
        this.loop();
    }


    private void loop() {

        // Set the clear color
        glClearColor(0.99f, 0.99f, 0.99f, 1.0f);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_COLOR_MATERIAL);
        glfwSwapInterval(0);
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        long previous = System.currentTimeMillis();
        long frames = 0;
        long prevSecFPS = 0;
//        this.readBestCar();

        int infoLineHeight = 15;

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


            DrawHeat.DrawHeatGrid(HEAT_GRID);
            if (!Constant.PAUSE) {
                HEAT_GRID.moveHeat();
            } else {
                Draw.drawAnyText("PAUSED", 15, infoLineHeight * 5, 0);
            }

            frames++;
            if (System.currentTimeMillis() - previous > 1000) {
//                System.out.printf("%d FPS %n", frames);
                previous = System.currentTimeMillis();
                prevSecFPS = frames;
                frames = 0;
            }


//            Draw.drawInfoTexts(prevSecFPS, processor, infoLineHeight);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            try {
                Thread.sleep(Constant.INTER_FRAME_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        saveBestCar();
    }



    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        long monitor = glfwGetPrimaryMonitor();

        try (MemoryStack s = stackPush()) {
            FloatBuffer px = s.mallocFloat(1);
            FloatBuffer py = s.mallocFloat(1);

            glfwGetMonitorContentScale(monitor, px, py);
        }

        // Create the window
        this.window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "Hello World!", NULL, NULL);
        if (this.window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetWindowSizeCallback(window, this::windowSizeChanged);

        GLFWVidMode vidmode = Objects.requireNonNull(glfwGetVideoMode(monitor));

        glfwSetWindowPos(
                window,
                (vidmode.width() - WINDOW_WIDTH) / 2,
                (vidmode.height() - WINDOW_HEIGHT) / 2
        );


        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        // Enable v-sync
        glfwSwapInterval(Constant.VSYNC);

        // Make the window visible
        glfwShowWindow(window);

    }

    private void callBacks() {
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (action == GLFW_RELEASE) {
                switch (key) {
                    case GLFW_KEY_ESCAPE:
                    case GLFW_KEY_Q:
                        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
                        break;
//                    case GLFW_KEY_D:
//                        Constant.DRAW_LEVEL = Constant.DRAW_LEVEL.getNext();
//                        break;
                    case GLFW_KEY_P:
                        Constant.PAUSE = !Constant.PAUSE;
                        break;
//                    case GLFW_KEY_UP:
//                        Draw.GLOBAL_VERTICAL_DISPLACEMENT += 10;
//                        break;
//                    case GLFW_KEY_DOWN:
//                        Draw.GLOBAL_VERTICAL_DISPLACEMENT -= 10;
//                        break;
//                    case GLFW_KEY_LEFT:
//                        Draw.GLOBAL_HORIZONTAL_DISPLACEMENT -= 10;
//                        break;
//                    case GLFW_KEY_RIGHT:
//                        Draw.GLOBAL_HORIZONTAL_DISPLACEMENT += 10;
//                        break;
//                    case GLFW_KEY_M:
//                        TRACK_MOUSE = !TRACK_MOUSE;
//                        break;
//                    case GLFW_KEY_O:
//                        TRACK.printSupplementalLines();
//                        break;
//                    case GLFW_KEY_T:
//                        DRAW_THETA = !DRAW_THETA;
//                        break;
                    case GLFW_KEY_V:
                        if (Constant.VSYNC == 0) {
                            Constant.VSYNC = 1;
                        } else {
                            Constant.VSYNC = 0;
                        }
                        glfwSwapInterval(Constant.VSYNC);
                }
            }
            if (action == GLFW_REPEAT) {
            }
        });
        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if (action == GLFW_PRESS) {
                this.saveMouseStart();
            }
//            if (TRACK_MOUSE && action == GLFW_PRESS) {
//                this.saveMouseStart();
//            }
//            if (TRACK_MOUSE && action == GLFW_RELEASE) {
//                this.saveMouseEnd();
//            }
        });
    }

    public static Point MOUSE_START = null;
    public static Point MOUSE_END = null;

//    private void saveMouseEnd() {
//        Color drawingColor = new Color(255, 165, 0);
//        double rot = 0;
//        Point directionVector = null;
//
//        LOGGER.info("mouse release");
//        MOUSE_END = getMousePoint();
////        Draw.drawnLines.add(new Line(MOUSE_START, MOUSE_END, drawingColor));
//        LOGGER.info("Start: {} | End: {}", MOUSE_START, MOUSE_END);
////        Draw.drawCrossMark(MOUSE_START);
////        Draw.drawCrossMark(MOUSE_END);
//
//        directionVector = getTwoPointDirectionVector(MOUSE_START, MOUSE_END);
//
//        LOGGER.info("Adding line");
//        Point leftBorderNewEnd;
//        Point rightBorderNewEnd;
////        int s = TRACK.borderTwo.size() - 1;
////        int t = TRACK.borderOne.size() - 1;
//
//        double abDist = Math.hypot((double) MOUSE_END.X - MOUSE_START.X, (double) MOUSE_END.Y - MOUSE_START.Y);
//        double bcDist = 50;
//        double acDist = Math.sqrt(Math.pow(abDist, 2) + Math.pow(bcDist, 2));
//        leftBorderNewEnd = new Point(
//                (int) (MOUSE_START.X + (directionVector.X * (acDist / abDist))),
//                (int) (MOUSE_START.Y + (directionVector.Y * (acDist / abDist))));
//
//        rightBorderNewEnd = new Point(
//                (int) (MOUSE_START.X + (directionVector.X * (acDist / abDist))),
//                (int) (MOUSE_START.Y + (directionVector.Y * (acDist / abDist))));
//
//        rot = Math.sin(bcDist / acDist);
//
//        leftBorderNewEnd = Draw.matRotatePoint(MOUSE_START, leftBorderNewEnd, rot);
//        rightBorderNewEnd = Draw.matRotatePoint(MOUSE_START, rightBorderNewEnd, -rot);
////        Line newLeftBorderNewLine = new Line(TRACK.borderTwo.get(s).E, leftBorderNewEnd);
////        Line newRightBorderNewLine = new Line(TRACK.borderOne.get(t).E, rightBorderNewEnd);
////        TRACK.addToBorderTwo(newLeftBorderNewLine);
////        TRACK.addToBorderOne(newRightBorderNewLine);
//        MOUSE_START = MOUSE_END;
//    }

    private void saveMouseStart() {
        Point mousePos = getMousePoint();
        LOGGER.info("{}|{} -> {} | {}", mousePos.X, mousePos.Y, HEAT_GRID.grid[mousePos.X][mousePos.Y].temperature, HEAT_GRID.grid[mousePos.X][mousePos.Y].getColor() );
    }

    private Point getMousePoint() {
        DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, b1, b2);
        return new Point(b1.get(0) / DISPLAY_WIDTH_SPACING, b2.get(0) / DISPLAY_HEIGHT_SPACING);
//        int x = (int) b1.get(0) - GLOBAL_HORIZONTAL_DISPLACEMENT;
//        int y = (int) b2.get(0) - GLOBAL_VERTICAL_DISPLACEMENT;
//        return new Point(x, y);
    }

    private void windowSizeChanged(long window, int width, int height) {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, WINDOW_WIDTH, WINDOW_HEIGHT, 0.0, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);
    }

    public Point getTwoPointDirectionVector(Point a, Point b) {
        return new Point(b.X - a.X, b.Y - a.Y);
    }


}
