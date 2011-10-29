package screencapture;

import java.awt.image.BufferedImage;

/**
 *
 * @author JJ_2011
 */
public class PicNode 
{    
    private BufferedImage img;
    private String fileName;
    
    public PicNode(BufferedImage img, String fileName)
    {
        this.img = img;
        this.fileName = fileName;
    }
    
    public synchronized BufferedImage getImage()
    {
        return this.img;
    }
    
    public synchronized String getFilePath()
    {
        return this.fileName;
    }
}