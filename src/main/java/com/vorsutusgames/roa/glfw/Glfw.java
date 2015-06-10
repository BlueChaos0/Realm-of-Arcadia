/*
 * Copyright 2015 Tyler Schuster
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vorsutusgames.roa.glfw;

import java.nio.ByteBuffer;
import com.vorsutusgames.roa.Game;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author Schuster
 */
public class Glfw {
	//	GLFWWindowFocusCallback windowFocusCallback;
	//	GLFWWindowCloseCallback windowCloseCallback;
	//	GLFWMonitorCallback monitorCallback;
	//	GLFWFramebufferSizeCallback framebufferSizeCallback;
	//	GLFWCursorEnterCallback cursorEnterCallback;
	//	GLFWCharCallback charCallback;
	//	GLFWDropCallback dropCallback;
	//	GLFWCharModsCallback charModsCallback;
	//	GLFWScrollCallback scrollCallback;
	//	GLFWMouseButtonCallback mouseButtonCallback;
	//	GLFWCursorPosCallback cursorPosCallback;
	public static long mainWindowId;
	private static boolean init = false;
	private static GLFWErrorCallback errorCallback;
	private static GLFWKeyCallback keyCallback;

	public static long init() {
		// Exit in case this has already been called
		if(init) return 0L;
		init = true;

		// Begin setup
		errorCallback = glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		if(glfwInit() != GL_TRUE) throw new IllegalStateException("Error with GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		// Create window
		//mainWindowId = glfwCreateWindow(Config.getInt("core.graphics.width"), Config.getInt
		// ("core.graphics.height"), "Realm of Arcadia", NULL, NULL);
		mainWindowId = glfwCreateWindow(640, 480, "Realm of Arcadia", NULL, NULL);

		if(mainWindowId == NULL) throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or
		// released.
		glfwSetKeyCallback(mainWindowId, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window,
							GL_TRUE); // We will detect this in our rendering loop
			}
		});

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(mainWindowId, (GLFWvidmode.width(vidmode) - 640) / 2,
				(GLFWvidmode.height(vidmode) - 480) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(mainWindowId);
		// Enable v-sync
		glfwSwapInterval(1);
		// Make the window visible
		glfwShowWindow(mainWindowId);
		return mainWindowId;
	}

	public static void destroy() {
		if(Game.INSTANCE.running)
			throw new IllegalStateException("Cannot destroy Glfw while game is running!");
		glfwDestroyWindow(mainWindowId);
		keyCallback.release();
		glfwTerminate();
		errorCallback.release();
	}
}
