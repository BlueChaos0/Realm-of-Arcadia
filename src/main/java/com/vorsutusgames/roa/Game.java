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

package com.vorsutusgames.roa;

import com.vorsutusgames.roa.glfw.Glfw;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GLContext;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {

	public static final Game INSTANCE = new Game();
	public boolean running = true;
	// The window handle
	private long window;

	private Game() {}

	public static void main(String[] args) {
		INSTANCE.run();
	}

	private void run() {
		System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
		init();
		loop();
		running = false;
		Glfw.destroy();
	}

	private void init() {
		window = Glfw.init();
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the ContextCapabilities instance and makes the OpenGL
		// bindings available for use.
		GLContext.createFromCurrent();

		// Set the clear color
		glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while(glfwWindowShouldClose(window) == GL_FALSE) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			glfwSwapBuffers(window); // swap the color buffers
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

}