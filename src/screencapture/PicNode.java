package screencapture;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

/**
 * @author JJ_2011
 */
public class PicNode
{    
    public final BufferedImage IMAGE;
    public final String FILE_NAME;
    
    public PicNode(BufferedImage IMAGE, String FILE_NAME)
    {
        this.IMAGE = IMAGE;
        this.FILE_NAME = FILE_NAME;
    }
    
    /**
     * Converts the BufferedImage into bytes.
     * @return Returns the bytes of a buffered Image.
     */
    public byte[] getImageBytes(byte[] buff, ByteArrayOutputStream bos)
    {
        buff = null;
        try
        {
            ImageIO.write(IMAGE, "png", bos);
            buff = bos.toByteArray();
        }
        catch(Exception ex)
        {
            System.out.println("Problem Serializeing PicNode");
            ex.printStackTrace();
        }
        return buff;
    }
}