/*
 * Wad.java
 * This file is part of jwadlib.
 * 
 * jwadlib WAD Library - A Java(TM) library for manipulating WAD files.
 * Copyright (C) 2008 Samuel "insertwackynamehere" Horwitz
 * 
 * jwadlib is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * jwadlib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package jwadlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

/**
 * The {@link Wad Wad} class is used to virtually open WAD files.  It allows for the manipulation of a WAD file on the Java platform.
 * @author Samuel "insertwackynamehere" Horwitz (@version 1.0 Alpha 2)
 * @author @picttarge (@version 1.0.1)
 * @version 1.0.1
 * @since 1.0
 */
public class Wad {
    //Private Variables
    /**
     * The WAD file as a {@link java.io.File File}.
     * @since 1.0
     */
    private File wadfilelocation;
    
    /**
     * The WAD file as a {@link java.io.RandomAccessFile RandomAccessFile}.
     * @since 1.0
     */
    private RandomAccessFile wadfile;
    
    /**
     * The {@link java.nio.channels.FileChannel FileChannel} associated with the 
     * {@link #wadfile WAD file}.
     * @since 1.0
     */
    private FileChannel wadfilechannel;
    
    //Protected Variables
    /**
     * A {@link LinkedList LinkedList} of the {@link Lump Lumps} in the {@link 
     * Wad Wad}.
     * @since 1.0
     */
    protected LinkedList<Lump> lumps;
    
    /**
     * The first four bytes of the WAD file.
     * @since 1.0
     */
    protected int identifier;
    
    //Constructors
    /**
     * Creates a {@link Wad Wad} object from a wad file as specified from the filepath.
     * @param filepath the location of the wad file including the name and extension.
     * @throws java.io.FileNotFoundException if the WAD file cannot be found.
     * @throws jwadlib.UnableToReadWADFileException if the WAD file cannot be read.
     * @since 1.0
     */ 
    public Wad(String filepath) throws FileNotFoundException, UnableToReadWADFileException {
        this(new File(filepath));
    }
    
    /**
     * Creates a {@link Wad Wad} object from a wad file that has been already pointed to by 
     * a {@link java.io.File File} object.
     * @param file a {@link java.io.File File} object that points to a wad file.
     * @throws java.io.FileNotFoundException if the WAD file cannot be found.
     * @throws jwadlib.UnableToReadWADFileException if the WAD file cannot be read.
     * @since 1.0
     */
    public Wad(File file) throws FileNotFoundException, UnableToReadWADFileException {
        wadfilelocation = file;
        wadfile = new RandomAccessFile(file, "r");
        wadfilechannel = wadfile.getChannel();

        WadByteBuffer header = new WadByteBuffer(wadfilechannel, 12, 0);
        identifier = header.getInt(0);
        WadByteBuffer directory = new WadByteBuffer(wadfilechannel, header.getInt(4)*16, header.getInt(8));
        lumps = new LinkedList<>();
        
        //Adds all of the lumps in the WAD file to the Wad object's LinkedList of lump objects.
        for(int i=0; i<header.getInt(4); i++) {
            int pointer = directory.getInt();
            int size = directory.getInt();
            String name = directory.getEightByteString();
            
            /* This should never occur because a basic Lump's initialization 
             * method always will return true.
             */
            try {
                lumps.add(new Lump(name, size, wadfilechannel, pointer));
            } catch(UnableToInitializeLumpException e) {
                throw new UnableToReadWADFileException("A lump in the WAD file could not be intialized.", e);
            }
        }
    }
    
    //Private Methods
    /**
     * Returns a {@link WadByteBuffer WadByteBuffer} containing the directory.
     * @return the {@link WadByteBuffer WadByteBuffer} containing the directory.
     * @since 1.0
     */
    private WadByteBuffer buildDirectory() {
        int directorylength = 16*lumps.size();
        int directorystart = 12;
        
        /* The lumpitr controls the pointer to the next lump.
         * Every directory entry has a pointer to the lump being referenced and
         * this var keeps track of where the next lump in the list will begin at.
         */
        int lumpitr = directorystart+directorylength;
        WadByteBuffer directory = new WadByteBuffer(directorylength);
        Lump current;
        
        /* Runs through each lump in the list and writes a directory entry to the 
         * directory buffer for each. Also, it sets the lumpitr's position for the 
         * next lump pointer.
         */
        for(int i=0; i<lumps.size(); i++) {
            current = lumps.get(i);
            directory.putInt(lumpitr);
            directory.putInt(current.getSize());
            directory.putEightByteString(current.getName());
            lumpitr += current.getSize();
        }
        return directory;
    }
    
    //Public Methods
    /**
     * Returns the {@link java.nio.channels.FileChannel FileChannel} of the WAD file.
     * @return the {@link java.nio.channels.FileChannel FileChannel} of the WAD file.
     * @since 1.0
     */
    public FileChannel getWadFileChannel() {
        return wadfilechannel;
    }
    
    /**
     * Returns a {@link RandomAccessFile RandomAccessFile} of the WAD 
     * that is represented by the {@link Wad Wad} object.
     * @return a {@link RandomAccessFile RandomAccessFile} of the WAD 
     * that is represented by the {@link Wad Wad} object.
     * @since 1.0
     */
    public RandomAccessFile getWad() {
        return wadfile;
    }
    
    /**
     * Returns the first four bytes of the WAD file.
     * @return the first four bytes of the WAD file.
     * @since 1.0
     */
    public int getWadIdentifier() {
        return identifier;
    }
    
    /**
     * Returns the number of lumps in the WAD, as specified in the header.
     * @return the number of lumps in the WAD, as specified in the header.
     * @since 1.0
     */
    public int getNumberOfLumps() {
        return lumps.size();
    }
    
    /**
     * Returns an {@link java.util.LinkedList LinkedList} of every 
     * {@link Lump Lump} in the WAD.
     * @return an {@link java.util.LinkedList LinkedList} of every 
     * {@link Lump Lump} in the WAD.
     * @since 1.0
     */
    public LinkedList<Lump> getAllLumps() {
        return lumps;
    }
    
    /**
     * Adds a {@link Lump Lump} object to the {@link Wad Wad}.
     * @param lump the {@link Lump Lump} to the {@link Wad Wad}.
     * @return true if the {@link Lump Lump} is successfully added.
     * @since 1.0
     */
    public boolean addLump(Lump lump) {
        lumps.add(lump);
        return true;
    }
}