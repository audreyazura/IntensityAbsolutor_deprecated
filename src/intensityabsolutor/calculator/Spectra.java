/*
 * Copyright (C) 2020 Alban Lafuente
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package intensityabsolutor.calculator;

import commonutils.ContinuousFunction;
import commonutils.PhysicsTools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.DataFormatException;
import org.nevec.rjm.BigComplex;

/**
 *
 * @author Alban Lafuente
 */
public class Spectra extends ContinuousFunction
{
    static public Spectra spectraFromWinSpec(File p_winSpecFile) throws FileNotFoundException, DataFormatException, ArrayIndexOutOfBoundsException, IOException
    {
        return new Spectra(p_winSpecFile, PhysicsTools.UnitsPrefix.NANO.getMultiplier(), PhysicsTools.UnitsPrefix.UNITY.getMultiplier(), "txt", " ", 3, new int[] {0,2});
    }
    static public Spectra callibrationAbsoluteIntensitySpectra(File p_passedFile) throws FileNotFoundException, DataFormatException, ArrayIndexOutOfBoundsException, IOException
    {
        return new Spectra(p_passedFile, PhysicsTools.UnitsPrefix.NANO.getMultiplier(), PhysicsTools.UnitsPrefix.UNITY.getMultiplier().divide(PhysicsTools.UnitsPrefix.NANO.getMultiplier()), "intensity", "\t", 2, new int[] {0,1});
    }
    
    public Spectra()
    {
        super();
    }
    
    public Spectra(Spectra p_passedSpectra)
    {
        super((ContinuousFunction) p_passedSpectra);
    }
    
    public Spectra(ContinuousFunction p_passedFunction)
    {
        super(p_passedFunction);
    }
    
    private Spectra (File p_inputFile, BigDecimal p_abscissaUnitMultiplier, BigDecimal p_valuesUnitMultiplier, String p_expectedExtension, String p_separator, int p_ncolumn, int[] p_columnToExtract) throws FileNotFoundException, DataFormatException, ArrayIndexOutOfBoundsException, IOException
    {
        super(p_inputFile, p_abscissaUnitMultiplier, p_valuesUnitMultiplier, p_expectedExtension, p_separator, p_ncolumn, p_columnToExtract);
    }
    
    public void logToFile(File outputFile) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        Set<BigDecimal> abscissa = new TreeSet(m_values.keySet());
        
        writer.write("Wavelength (nm)\tAbsolute intensity (μW/nm)");
        for(BigDecimal currentAbscissa: abscissa)
        {
            BigDecimal convertedIntensity = currentAbscissa.multiply(PhysicsTools.UnitsPrefix.NANO.getMultiplier()).divide(PhysicsTools.UnitsPrefix.MICRO.getMultiplier());
            
            writer.newLine();
            writer.write(currentAbscissa.divide(PhysicsTools.UnitsPrefix.NANO.getMultiplier())+"\t"+convertedIntensity);
        }
        writer.flush();
        writer.close();
    }
    
    public Spectra selectWindow(BigDecimal p_minAbscissa, BigDecimal p_maxAbscissa)
    {
        Spectra resizedSpectra = new Spectra();
        
        for(BigDecimal abscissa: m_values.keySet())
        {
            if(abscissa.compareTo(p_minAbscissa) < 0 || abscissa.compareTo(p_maxAbscissa) > 0)
            {
                m_values.remove(abscissa);
            }
        }
        
        return resizedSpectra;
    }
}
