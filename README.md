# Engine
A basic game engine, easy to use with a transparent backend.

## Graphics
This engine uses basic AWT Graphics2D objects to render to a double buffered applet.
Rendering to the screen is just like AWT with the addition of some useful functions.

## Game Loop
The game loop consists of a call to first(), followed by repeated calls to loop()
controlled by a system timing mechanism, and occasional calls to delay() based on
the delay set by setDelay(). A debug() method can draw debug information on top of
the normal loop() graphics if enabled by setDebug().
The game loop doesn't distinguish between logical and graphical code segments, so
you need to keep render order in mind when writing your game code.

## Source Material
Source material can be images or text files read in by the readImage() and getFiles()
methods respectively. This supports any image format supported by ImageIO and 
BufferedImage, and any extension, provided the file is in fact a text file.

## User Input
User input is provided through AWT Listeners, which can be used directly or in 
conjunction with the Controller class. There is support for mouse and keyboard
inputs.

## Controller
There is a controller class specifically meant to make reading and updating user input
easy. Names are bound to inputs through a double mapping which allows in-game rebinding.

## Physics
Physics are included as a modular and optional component of the engine, implemented
using verlet integration. Position, acceleration, and pivoting bodies can be created
using this system.

## Procedural Map Generation
Maps can be generated using a Map class which utilizes Kurt Spencer's amazing Java
implementation of OpenSimplexNoise. Make new maps with custom octaves centered at
different altitudes, layering multiple joined maps for altitude with layered disjoint
maps for humidity, a shader can then be applied using the Shader class to block in the
map's color and texture.

## Shader
The Shader is informed by a custom image which allows easy two dimensional shader rules
to dictate map coloring.
