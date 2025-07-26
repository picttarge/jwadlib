# jwadlib 1.0.1

Fork of an old jwadlib (1.0-Alpha).

jwadlib WAD Library is a Java library for manipulating WAD files, the format made popular by Doom.


# Changes from the original
* Original: http://sourceforge.net/projects/jwadlib/ Copyright (C) 2008 Samuel "insertwackynamehere" Horwitz
* Changed: https://github.com/picttarge/jwadlib Copyright (C) 2025 @picttarge


* libGDX file handling
* JDK 17 (libGDX 1.13.5 compatibility)
* Removes some undesirable alpha features completely from the original project

## Changelog

### 26 July 2025 @picttarge

* Adds WadReader and support for libGDX file handling (FileHandle/Gdx.files.internal)
* Removes Wad.equals()
* Removes all write actions e.g. Wad.writeToFile()
* Removes the writing of .bak files as a backup wad file and UnableToBackUpWADFileException
* Removes "rw" (read-write) files access on RandomAccessFile, defaulting to "r" (read-only)


# License
GNU Library or Lesser General Public License version 2.0 (LGPLv2)

# Original README.md text follows

jwadlib WAD Library - A Java(TM) library for manipulating WAD files.
Copyright (C) 2008 Samuel "insertwackynamehere" Horwitz

jwadlib is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

jwadlib is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

--------------------------------------------------------------------------------

Developed for Java(TM) 5.0

jwadlib - Version 1.0 Alpha 2
<http://sourceforge.net/projects/jwadlib/>
Please report all bugs to the bug trackers at the above URL!

Copyright 2008 Samuel "insertwackynamehere" Horwitz
<samhorwitz@users.sourceforge.net>

--------------------------------------------------------------------------------

Features:
-Can read and write WAD files on a simple level
-Can compare to WAD files loaded into memory
-Can save lumps to a file
-Can get info and content of lumps
-Can get info of WAD files

Bugs:
-Some issues with comparing WAD files. If the equals method does not seem to
work, then try removing all other method calls and see if it works. It has
some problems when being called after a Wad.writeToFile() call.
-Sometimes WadByteBuffer calls for gets (getByte(), getInt(), etc) crash due to
an error with indexing.
-Others, this is an alpha release.