// Catalano Android Imaging Library
// The Catalano Framework
//
// Copyright © Diego Catalano, 2012-2016
// diego.catalano at live.com
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//
package catalano.imaging.Filters;

import catalano.imaging.FastBitmap;
import catalano.imaging.IApplyInPlace;

/**
 * Also called Gray level reduction.
 * Image quantization is the process of reducing the image data by removing some of the detail information
 * by mapping groups of data points to a single point.
 * @author Diego Catalano
 */
public class ImageQuantization implements IApplyInPlace{
    
    private int level = 16;

    /**
     * Initialize a new instance of the ImageQuantization class.
     */
    public ImageQuantization() {}
    
    /**
     * Initialize a new instance of the ImageQuantization class.
     * @param level 
     */
    public ImageQuantization(int level){
        boolean isPower2 = catalano.math.Tools.isPowerOf2(level);
        if (isPower2) {
            this.level = Math.min(level, 256);
        }
        else{
            int x = catalano.math.Tools.NextPowerOf2(level);
            this.level = Math.min(x, 256);
        }
    }

    @Override
    public void applyInPlace(FastBitmap fastBitmap) {
        
        int width = fastBitmap.getWidth();
        int height = fastBitmap.getHeight();
        int reduced = 256 / (level - 1);
        int steps = 256 / reduced;
        
        if (fastBitmap.isGrayscale()) {
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    int gray = fastBitmap.getGray(x, y);
                    int index = reduced;
                    for (int z = 0; z < steps; z++) {
                        if((gray > z * reduced) && (gray <= index)) {
                            fastBitmap.setGray(x, y, z * reduced);
                        }
                        index += reduced;
                    }
                }
            }
        }
        else{
            try {
                throw new IllegalArgumentException("ImageQuantization only works with grayscale images");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}