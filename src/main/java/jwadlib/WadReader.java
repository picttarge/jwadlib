package jwadlib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * @author @picttarge
 */
public class WadReader {
    private final RandomAccessFile wadfile;
    private final LinkedList<Lump> lumps;
    private final int identifier;

    public WadReader(final FileHandle fileHandle) throws FileNotFoundException, UnableToReadWADFileException {
        this(fileHandle.file());
    }

    public WadReader(final String filepath) throws FileNotFoundException, UnableToReadWADFileException {
        this(Gdx.files.internal(filepath).file());
    }

    public WadReader(final File file) throws FileNotFoundException, UnableToReadWADFileException {
        this.wadfile = new RandomAccessFile(file, "r");

        final FileChannel wadfilechannel = this.wadfile.getChannel();

        final WadByteBuffer header = new WadByteBuffer(wadfilechannel, 12, 0);
        this.identifier = header.getInt(0);
        final WadByteBuffer directory = new WadByteBuffer(wadfilechannel, header.getInt(4) * 16, header.getInt(8));
        this.lumps = new LinkedList<>();

        try {
            for(int i = 0; i < header.getInt(4); ++i) {
                final int pointer = directory.getInt();
                final int size = directory.getInt();
                final String name = directory.getEightByteString();
                this.lumps.add(new Lump(name, size, wadfilechannel, pointer));
            }
        } catch (UnableToInitializeLumpException e) {
            throw new UnableToReadWADFileException("A lump in the WAD file could not be initialized.", e);
        }
    }

    public RandomAccessFile getWad() {
        return this.wadfile;
    }

    public int getWadIdentifier() {
        return this.identifier;
    }

    public int getNumberOfLumps() {
        return this.lumps.size();
    }

    public List<Lump> getAllLumps() {
        return this.lumps;
    }
}
