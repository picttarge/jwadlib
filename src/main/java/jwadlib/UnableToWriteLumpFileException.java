/*
 * UnableToWriteLumpFileException.java
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

/**
 * Signals that the lump file is unable to be written.
 * @author Samuel "insertwackynamehere" Horwitz
 * @version 1.0 Alpha 2
 * @since 1.0
 */
public class UnableToWriteLumpFileException extends WADFormatException {    
    /**
     * Constructs an {@link java.lang.Exception Exception} without a message 
     * or a {@link java.lang.Throwable Throwable} cause.
     * @since 1.0
     */
    public UnableToWriteLumpFileException() {
        super();
    }
    
    /**
     * Constructs an {@link java.lang.Exception Exception} with a message 
     * but no {@link java.lang.Throwable Throwable} cause.
     * @param message the description of the {@link java.lang.Exception 
     * Exception}.
     * @since 1.0
     */
    public UnableToWriteLumpFileException(String message) {
        super(message);
    }
    
    /**
     * Constructs an {@link java.lang.Exception Exception} with a {@link 
     * java.lang.Throwable Throwable} cause but no message.
     * @param cause the parent {@link java.lang.Throwable Throwable} that 
     * spawned this exception.
     * @since 1.0
     */
    public UnableToWriteLumpFileException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs an {@link java.lang.Exception Exception} with a message 
     * and a {@link java.lang.Throwable Throwable} cause.
     * @param message the description of the {@link java.lang.Exception 
     * Exception}.
     * @param cause the parent {@link java.lang.Throwable Throwable} that 
     * spawned this exception.
     * @since 1.0
     */
    public UnableToWriteLumpFileException(String message, Throwable cause) {
        super(message, cause);
    }
}