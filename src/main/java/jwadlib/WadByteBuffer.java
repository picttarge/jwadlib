/*
 * WadByteBuffer.java
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

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * A wrapper class for the {@link java.nio.ByteBuffer ByteBuffer} class that 
 * specifically is designed to work with WAD file data. All data is read in {@link 
 * java.nio.ByteOrder#LITTLE_ENDIAN little endian} mode.
 * @author Samuel "insertwackynamehere" Horwitz
 * @version 1.0 Alpha 2
 * @since 1.0
 */
public class WadByteBuffer {
    //Private Variables
    /**
     * The {@link java.nio.ByteBuffer ByteBuffer} that backs the {@link WadByteBuffer 
     * WadByteBuffer}.
     * @since 1.0
     */
    private ByteBuffer bytebuffer;
    
    /**
     * The number of bytes this {@link WadByteBuffer WadByteBuffer} contains in 
     * it's {@link #bytebuffer ByteBuffer}.
     * @since 1.0
     */
    private int length;
    
    //Constructors
    /**
     * Creates an empty {@link WadByteBuffer WadByteBuffer} of a set size.
     * @param buffersize the size, in bytes, to allocate for the buffer.
     * @since 1.0
     */
    public WadByteBuffer(int buffersize) {
        bytebuffer = ByteBuffer.allocate(buffersize);
        bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
        length = 0;
        bytebuffer.position(0);
    }
    
    /**
     * Creates a {@link WadByteBuffer WadByteBuffer} from a {@link java.nio.channels.FileChannel 
     * FileChannel} from the beginning to the end of the channel.
     * @param bufferchannel the {@link java.nio.channels.FileChannel FileChannel} 
     * to read from.
     * @throws jwadlib.UnableToReadWADFileException if the WAD file cannot be read from or to 
     * the specified points.
     * @since 1.0
     */
    public WadByteBuffer(FileChannel bufferchannel) throws UnableToReadWADFileException {
        this(bufferchannel, -1, 0);
    }
    
    /**
     * Creates a {@link WadByteBuffer WadByteBuffer} from a {@link java.nio.channels.FileChannel 
     * FileChannel} from the beginning of the channel until the number of specified bytes are read.
     * @param bufferchannel the {@link java.nio.channels.FileChannel FileChannel} 
     * to read from. 
     * @param bufferlength the number of bytes to read from the starting position 
     * of the {@link java.nio.channels.FileChannel FileChannel}.
     * @throws jwadlib.UnableToReadWADFileException if the WAD file cannot be read from or to
     * the specified points.
     * @since 1.0
     */
    public WadByteBuffer(FileChannel bufferchannel, int bufferlength) throws UnableToReadWADFileException {
        this(bufferchannel, bufferlength, 0);
    }
    
    /**
     * Creates a {@link WadByteBuffer WadByteBuffer} from a {@link java.nio.channels.FileChannel 
     * FileChannel} from the indicated position until the number of specified bytes are read.
     * @param bufferchannel the {@link java.nio.channels.FileChannel FileChannel} 
     * to read from. 
     * @param bufferlength the number of bytes to read from the starting position 
     * of the {@link java.nio.channels.FileChannel FileChannel}.
     * @param position the starting position to read from the {@link java.nio.channels.FileChannel 
     * FileChannel}.
     * @throws jwadlib.UnableToReadWADFileException if the WAD file cannot be read from or to
     * the specified points.
     * @since 1.0
     */
    public WadByteBuffer(FileChannel bufferchannel, int bufferlength, int position) throws UnableToReadWADFileException {
        try {
            bufferchannel.position(position);
        }
        catch(IOException e) {
            throw new UnableToReadWADFileException("Position out of file bounds.", e);
        }
        if(bufferlength < 0) {
            try {
                bufferlength = (int)bufferchannel.size();
            }
            catch(IOException e) {
                throw new UnableToReadWADFileException("WAD file cannot be read.", e);
            }
        }
        bytebuffer = ByteBuffer.allocate(bufferlength);
        bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
        try {
            bufferchannel.read(bytebuffer, position);
        }
        catch(IOException e) {
            throw new UnableToReadWADFileException("WAD file cannot be read.", e);
        }
        length = bufferlength;
        bytebuffer.position(0);
    }
    
    /**
     * Creates a {@link WadByteBuffer WadByteBuffer} from another {@link WadByteBuffer 
     * WadByteBuffer}.
     * @param bytebuffer the {@link WadByteBuffer WadByteBuffer} to create the new 
     * {@link WadByteBuffer WadByteBuffer} from.
     * @since 1.0
     */
    public WadByteBuffer(WadByteBuffer bytebuffer) {
        this(bytebuffer.getArray());
    }
    
    /**
     * Creates a {@link WadByteBuffer WadByteBuffer} from an array of bytes.
     * @param bytearray the array of bytes to create the {@link WadByteBuffer WadByteBuffer} 
     * from.
     * @since 1.0
     */
    public WadByteBuffer(byte[] bytearray) {
        bytebuffer = ByteBuffer.wrap(bytearray);
        bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
        length = bytearray.length;
        bytebuffer.position(0);
    }
    
    //Private Methods
    /**
     * Returns an array of bytes containing the contents of the {@link #bytebuffer 
     * ByteBuffer}.
     * @return an array of bytes containing the contents of the {@link #bytebuffer 
     * ByteBuffer}.
     * @since 1.0
     */
    private byte[] getArray() {
        byte[] temp;
        if(bytebuffer.hasArray()) {
            temp = bytebuffer.array();
        }
        else {
            temp = null;
        }
        return temp;
    }
    
    //Public Methods
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#get() get()} method; gets the next byte in the {@link 
     * WadByteBuffer WadByteBuffer}.
     * @return the next byte in the {@link WadByteBuffer WadByteBuffer}.
     * @since 1.0
     */
    public byte getByte() {
        return bytebuffer.get();
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#get(int) get(int)} method; gets the next byte in the {@link 
     * WadByteBuffer WadByteBuffer}, starting at the specified position. 
     * Unlike the method it wraps, this method increments the position counter.
     * @param index the starting position of the of the {@link WadByteBuffer 
     * WadByteBuffer}.
     * @return the next byte in the {@link WadByteBuffer WadByteBuffer}, from 
     * the specified position.
     * @since 1.0
     */
    public byte getByte(int index) {
        byte temp = bytebuffer.get(index);
        setPosition(index+1);
        return temp;
    }
    
    /**
     * Returns the backing {@link java.nio.ByteBuffer ByteBuffer} of the {@link 
     * WadByteBuffer WadByteBuffer}.
     * @return the backing {@link java.nio.ByteBuffer ByteBuffer} of the {@link 
     * WadByteBuffer WadByteBuffer}.
     * @since 1.0
     */
    public ByteBuffer getByteBuffer() {
        ByteBuffer temp = bytebuffer;
        temp.position(0);
        return temp;
    }
    
    /**
     * Gets a sub-{@link WadByteBuffer WadByteBuffer} out of the current one, 
     * by getting a specified number of bytes starting at a specified position in 
     * the buffer.
     * @param offset the starting position for getting bytes in the current 
     * {@link WadByteBuffer WadByteBuffer}.
     * @param length the number of bytes to get from the starting position in the 
     * {@link WadByteBuffer WadByteBuffer}.
     * @return a new {@link WadByteBuffer WadByteBuffer} consisting of the bytes 
     * from the starting position to the ending position based on the number of bytes
     * to get.
     * @since 1.0
     */
    public WadByteBuffer getWadByteBuffer(int offset, int length) {
        byte[] temp = new byte[length];
        int oldpos = bytebuffer.position();
        bytebuffer.get(temp, offset, length);
        bytebuffer.position(oldpos);
        return new WadByteBuffer(temp);
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#getShort() getShort()} method; gets the next short in the {@link 
     * WadByteBuffer WadByteBuffer}.
     * @return the next short in the {@link WadByteBuffer WadByteBuffer}.
     * @since 1.0
     */
    public short getShort() {
        return bytebuffer.getShort();
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#getShort(int) getShort(int)} method; gets the next short in the {@link 
     * WadByteBuffer WadByteBuffer}, starting at the specified position.
     * Unlike the method it wraps, this method increments the position counter.
     * @param index the starting position of the of the {@link WadByteBuffer 
     * WadByteBuffer}.
     * @return the next short in the {@link WadByteBuffer WadByteBuffer}, from 
     * the specified position.
     * @since 1.0
     */
    public short getShort(int index) {
        short temp = bytebuffer.getShort(index);
        setPosition(index+2);
        return temp;
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#getInt() getInt()} method; gets the next integer in the {@link 
     * WadByteBuffer WadByteBuffer}.
     * @return the next integer in the {@link WadByteBuffer WadByteBuffer}.
     * @since 1.0
     */
    public int getInt() {
        return bytebuffer.getInt();
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#getInt(int) getInt(int)} method; gets the next integer in the {@link 
     * WadByteBuffer WadByteBuffer}, starting at the specified position.
     * Unlike the method it wraps, this method increments the position counter.
     * @param index the starting position of the of the {@link WadByteBuffer 
     * WadByteBuffer}.
     * @return the next integer in the {@link WadByteBuffer WadByteBuffer}, from 
     * the specified position.
     * @since 1.0
     */
    public int getInt(int index) {
        int temp = bytebuffer.getInt(index);
        setPosition(index+4);
        return temp;
    }
    
    /**
     * Gets the next eight bytes in the {@link WadByteBuffer WadByteBuffer} as a 
     * {@link java.lang.String String}, starting at the specified position. Each 
     * byte is a new character.
     * @param index the starting position of the of the {@link WadByteBuffer 
     * WadByteBuffer}.
     * @return the next eight byte {@link java.lang.String String}, from the 
     * specified position.
     * @since 1.0
     */
    public String getEightByteString(int index) {
        setPosition(index);
        return getEightByteString();
    }
    
    /**
     * Gets the next eight bytes in the {@link WadByteBuffer WadByteBuffer} as a 
     * {@link java.lang.String String}. Each byte is a new character.
     * @return the next eight byte {@link java.lang.String String}.
     * @since 1.0
     */
    public String getEightByteString() {
        String temp = "";
        for(int i=0; i<8; i++) {
            temp = temp + (char)bytebuffer.get();
        }
        return temp;
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#put(byte) put(byte)} method; puts a single byte into 
     * the {@link WadByteBuffer WadByteBuffer}.
     * @param b the byte to insert into the {@link WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.nio.BufferOverflowException if the current position is not smaller 
     * than the limit of the {@link #bytebuffer ByteBuffer}.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer put(byte b) throws BufferOverflowException, ReadOnlyBufferException {
        bytebuffer.put(b);
        length += 1;
        return this;
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#put(int, byte) put(int, byte)} method; puts a single byte into 
     * the {@link WadByteBuffer WadByteBuffer} at the specified position.
     * Unlike the method it wraps, this method increments the position counter.
     * @param index the position to insert the byte.
     * @param b the byte to insert into the {@link WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.lang.IndexOutOfBoundsException if index is not positive or out of 
     * the {@link #bytebuffer ByteBuffer's} limit.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer put(int index, byte b) throws IndexOutOfBoundsException, ReadOnlyBufferException {
        bytebuffer.put(index, b);
        length += 1;
        setPosition(index+1);
        return this;
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#put(ByteBuffer) put(ByteBuffer)} method; puts a {@link 
     * java.nio.ByteBuffer ByteBuffer} into the {@link WadByteBuffer WadByteBuffer}.
     * @param src the {@link WadByteBuffer WadByteBuffer} to insert.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.nio.BufferOverflowException if the current position is not smaller 
     * than the limit of the {@link #bytebuffer ByteBuffer}.
     * @throws java.lang.IllegalArgumentException if the source buffer and this buffer are 
     * the same.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer put(WadByteBuffer src) throws BufferOverflowException, IllegalArgumentException, ReadOnlyBufferException {
        bytebuffer.put(src.bytebuffer);
        length += src.getLength();
        return this;
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#putShort(short) putShort(short)} method; puts a short into 
     * the {@link WadByteBuffer WadByteBuffer}.
     * @param value the short to insert into the {@link WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.lang.IndexOutOfBoundsException if index is not positive or out of 
     * the {@link #bytebuffer ByteBuffer's} limit.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer putShort(short value) throws IndexOutOfBoundsException, ReadOnlyBufferException {
        bytebuffer.putShort(value);
        length += 2;
        return this;
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#putShort(short) putShort(short)} method; puts a short into 
     * the {@link WadByteBuffer WadByteBuffer}.
     * Unlike the method it wraps, this method increments the position counter.
     * @param index the position to insert the value.
     * @param value the short to insert into the {@link WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.lang.IndexOutOfBoundsException if index is not positive or out of 
     * the {@link #bytebuffer ByteBuffer's} limit.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer putShort(int index, short value) throws IndexOutOfBoundsException, ReadOnlyBufferException {
        bytebuffer.putShort(index, value);
        length += 2;
        setPosition(index+2);
        return this;
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#putInt(int) putInt(int)} method; puts an integer into 
     * the {@link WadByteBuffer WadByteBuffer}.
     * @param value the integer to insert into the {@link WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.lang.IndexOutOfBoundsException if index is not positive or out of 
     * the {@link #bytebuffer ByteBuffer's} limit.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer putInt(int value) throws IndexOutOfBoundsException, ReadOnlyBufferException {
        bytebuffer.putInt(value);
        length += 4;
        return this;
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#putInt(int) putInt(int)} method; puts an integer into 
     * the {@link WadByteBuffer WadByteBuffer}.
     * Unlike the method it wraps, this method increments the position counter.
     * @param index the position to insert the value.
     * @param value the integer to insert into the {@link WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.lang.IndexOutOfBoundsException if index is not positive or out of 
     * the {@link #bytebuffer ByteBuffer's} limit.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer putInt(int index, int value) throws IndexOutOfBoundsException, ReadOnlyBufferException {
        bytebuffer.putInt(index, value);
        length += 4;
        setPosition(index+4);
        return this;
    }
    
    /**
     * Puts eight bytes in the {@link WadByteBuffer WadByteBuffer} as a 
     * {@link java.lang.String String} at the specified position. Each byte is a 
     * new character.
     * @param index the position to insert the value.
     * @param value the {@link java.lang.String String} to insert into the {@link 
     * WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.lang.IndexOutOfBoundsException if index is not positive or out of 
     * the {@link #bytebuffer ByteBuffer's} limit.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer putEightByteString(int index, String value) throws IndexOutOfBoundsException, ReadOnlyBufferException {
        setPosition(index);
        return putEightByteString(value);
    }
    
    /**
     * Puts eight bytes in the {@link WadByteBuffer WadByteBuffer} as a 
     * {@link java.lang.String String}. Each byte is a new character.
     * @param value the {@link java.lang.String String} to insert into the {@link 
     * WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.lang.IndexOutOfBoundsException if index is not positive or out of 
     * the {@link #bytebuffer ByteBuffer's} limit.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer putEightByteString(String value) throws IndexOutOfBoundsException, ReadOnlyBufferException {
        return putEightByteString(value.toCharArray());
    }
    
    /**
     * Puts eight bytes in the {@link WadByteBuffer WadByteBuffer} as an array 
     * of characters, at the specified position. Each byte is a new character.
     * @param index the position to insert the value.
     * @param value the character array to insert into the {@link 
     * WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.lang.IndexOutOfBoundsException if index is not positive or out of 
     * the {@link #bytebuffer ByteBuffer's} limit.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer putEightByteString(int index, char[] value) throws IndexOutOfBoundsException, ReadOnlyBufferException {
        setPosition(index);
        return putEightByteString(value);
    }
    
    /**
     * Puts eight bytes in the {@link WadByteBuffer WadByteBuffer} as an array 
     * of characters. Each byte is a new character.
     * @param value the character array to insert into the {@link 
     * WadByteBuffer WadByteBuffer}.
     * @return the current {@link WadByteBuffer WadByteBuffer}.
     * @throws java.lang.IndexOutOfBoundsException if index is not positive or out of 
     * the {@link #bytebuffer ByteBuffer's} limit.
     * @throws java.nio.ReadOnlyBufferException if the {@link #bytebuffer ByteBuffer} cannot 
     * be written to.
     * @since 1.0
     */
    public WadByteBuffer putEightByteString(char[] value) throws IndexOutOfBoundsException, ReadOnlyBufferException {
        for(int i=0; i<8; i++) {
            bytebuffer.put((byte)value[i]);
        }
        length += 8;
        return this;
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#hasRemaining() hasRemaining()} method; returns true 
     * if the {@link WadByteBuffer WadByteBuffer} has any values left past the 
     * current position.
     * @return true 
     * if the {@link WadByteBuffer WadByteBuffer} has any values left past the 
     * current position, otherwise false.
     * @since 1.0
     */
    public boolean hasRemaining() {
        return bytebuffer.hasRemaining();
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#position() position()} method; returns the {@link 
     * WadByteBuffer WadByteBuffer's} position.
     * @return the position of the {@link WadByteBuffer WadByteBuffer}.
     * @since 1.0
     */
    public int getPosition() {
        return bytebuffer.position();
    }
    
    /**
     * A wrapper class for {@link java.nio.ByteBuffer ByteBuffer's} {@link 
     * java.nio.ByteBuffer#position(int) position(int)} method; sets the {@link 
     * WadByteBuffer WadByteBuffer's} position to the specified integer and returns 
     * the {@link WadByteBuffer WadByteBuffer} object that called it.
     * @param index the integer to set the {@link WadByteBuffer WadByteBuffer's} 
     * position to.
     * @return the {@link WadByteBuffer WadByteBuffer} object that called it.
     * @throws java.lang.IllegalArgumentException if the index is negative or larger 
     * than the current limit.
     * @since 1.0
     */
    public WadByteBuffer setPosition(int index) throws IllegalArgumentException {
        bytebuffer.position(index);
        return this;
    }
    
    /**
     * Returns the length of the {@link WadByteBuffer WadByteBuffer}.
     * @return the length of the {@link WadByteBuffer WadByteBuffer}.
     * @since 1.0
     */
    public int getLength() {
        return length;
    }
    
    /**
     * Returns the capacity of the {@link java.nio.ByteBuffer ByteBuffer}.
     * @return the capacity of the {@link java.nio.ByteBuffer ByteBuffer}.
     * @since 1.0
     */
    public int getCapacity() {
        return bytebuffer.capacity();
    }
    
    /**
     * Writes this {@link WadByteBuffer WadByteBuffer's} data to the specified channel.
     * @param filechannel the {@link java.nio.channels.FileChannel FileChannel} to 
     * write to.
     * @return true if the data is successfully written.
     * @throws java.io.IOException if the data cannot be written to the file.
     * @since 1.0
     */
    public boolean writeToFile(FileChannel filechannel) throws IOException {
        return writeToFile(filechannel, filechannel.position());
    }
    
    /**
     * Writes this {@link WadByteBuffer WadByteBuffer's} data to the specified channel, 
     * at the specified position.
     * @param filechannel the {@link java.nio.channels.FileChannel FileChannel} to 
     * write to.
     * @param position the position in the file to start writing the data.
     * @return true if the data is successfully written.
     * @throws java.io.IOException if the data cannot be written to the file.
     * @since 1.0
     */
    public boolean writeToFile(FileChannel filechannel, long position) throws IOException {
        int oldpos = bytebuffer.position();
        bytebuffer.position(0);
        filechannel.position(position);
        filechannel.write(bytebuffer);
        bytebuffer.position(oldpos);
        return true;
    }
    
    /**
     * Changes the size of the {@link WadByteBuffer WadByteBuffer's} backing 
     * {@link java.nio.ByteBuffer ByteBuffer}.
     * @param newsize the size the new {@link java.nio.ByteBuffer ByteBuffer} should 
     * be.
     * @return true if completed successfully.
     * @since 1.0
     */
    public boolean alterBufferSize(int newsize) {
        ByteBuffer tempbuffer = bytebuffer;
        bytebuffer = ByteBuffer.allocate(newsize);
        bytebuffer.rewind();
        bytebuffer.put(tempbuffer);
        bytebuffer.position(tempbuffer.position());
        tempbuffer = null;
        return true;
    }
    
    //Public Static Methods
    /**
     * Converts a {@link java.lang.String String} to a new eight byte {@link 
     * java.lang.String String} by padding {@link java.lang.String Strings} less 
     * than eight characters and truncating {@link java.lang.String Strings} greater 
     * than eight characters. All the letters are converted to uppercase and all 
     * illegal characters are trimmed out. The legal characters are capital A through
     * Z and 0 through 9 along with "[", "]", "-", "_", "\" and the null character.
     * @param string the {@link java.lang.String String} to convert to an eight 
     * byte {@link java.lang.String String}.
     * @return the new eight byte {@link java.lang.String String}.
     * @since 1.0
     */
    public static String convertToEightByteString(String string) {
        if(string.length() > 8) {
            string = string.substring(0, 8);
        }
        else if(string.length() < 8) {
            char temp[] = string.toCharArray();
            char newtemp[] = new char[8];
            for(int i=0; i<8; i++) {
                if(i<temp.length) {
                    newtemp[i] = temp[i];
                }
                else {
                    newtemp[i] = (char)0;
                }
            }
            string = String.valueOf(newtemp);
        }
        string = string.toUpperCase();
        string = string.replaceAll("[^A-Z0-9\\[\\]\\-_\\\\\\x00]", "-");
        return string;
    }
}