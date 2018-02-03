package nsoft.automata;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.omg.CORBA.portable.ValueInputStream;

import nsoft.automata.Cell.State;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MainGL {

	static final int gridSize = 5;

	static final int Width = 800;
	static final int Height = 600;
	
	static final int CWidth = Width/gridSize;
	static final int CHeight = Height/gridSize;
	
	static int X,Y;
	static Cell[][] cells = new Cell[800/gridSize][600/gridSize];
	
	// The window handle
	private long window;

	static Thread Simulation = new Thread(()->{
		
		try {
			
			while(true) {
				

				update();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	});

	public void run() {
		
		init();
		initCells();
		Simulation.setPriority(Thread.MAX_PRIORITY);
		Simulation.start();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private static void update() {
		
		for (int i = 0; i < CWidth; i++) {
			
			for (int j = 0; j < CHeight; j++) {
				
				int mi = i-1;
				int mj = j-1;
				
				int li = (i+1) % CWidth;
				int lj = (j+1) % CHeight;
				
				if(mi < 0) mi= CWidth -1;
				if(mj < 0) mj = CHeight -1;
				cells[i][j].act(
						
						cells[mi] [j],
						cells[i] [mj],
						cells[mi][mj],
						cells[li] [j],
						cells[i] [lj],
						cells[li] [lj]
						);
			}
		}
	}
	private static void initCells() {
		
		for (int i = 0; i < CWidth; i++) {
			
			for (int j = 0; j < CHeight; j++) {
				
				cells[i][j] = new Cell();
			}
		}
	}
	private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		
	    
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(Width, Height, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		glfwSetCursorPosCallback(window, (window,x,y)->{
			
			y = Height -y;
			if(x > Width -1 || y > Height -1)return;
	
			X = (int) (x/gridSize);
			Y = (int) (y/gridSize);
		});
		
		glfwSetMouseButtonCallback(window, (a,b,c,d)->{
			
			cells[X][Y].set(State.VIRUS);
		});
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		
		
		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop() {
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Width, 0, Height, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		
		while ( !glfwWindowShouldClose(window) ) {
			

			glClear(GL_COLOR_BUFFER_BIT);
			for (int i = 0; i < CWidth; i++) {
				
				for (int j = 0; j < CHeight; j++) {

					if(cells[i][j].isUpdated()) {
						
						if(cells[i][j].current == State.NONE) continue;
						glColor3f(cells[i][j].current.color[0],
								cells[i][j].current.color[1],
								cells[i][j].current.color[2]);
						glBegin(GL_QUADS);
						
							glVertex2f(i*gridSize, j*gridSize);
							glVertex2f((i + 1)*gridSize, j*gridSize);
							glVertex2f((i + 1)*gridSize, (j + 1)*gridSize);
							glVertex2f(i*gridSize, (j +1)*gridSize);
						glEnd();
						cells[i][j].update();
					}
				}
			}
			
			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
		
		System.out.println("exit");
		System.exit(0);
		Simulation.interrupt();
	}

	public static void main(String[] args) {
		new MainGL().run();
	}

}
