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
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 *
 * @author Alban Lafuente
 */
public class Spectra extends ContinuousFunction
{
    static public Spectra spectraFromWinSpec(File p_winSpecFile) throws FileNotFoundException, DataFormatException, ArrayIndexOutOfBoundsException, IOException
    {
//        return new Spectra(p_winSpecFile, PhysicsTools.UnitsPrefix.NANO.getMultiplier(), PhysicsTools.UnitsPrefix.UNITY.getMultiplier(), "txt", " ", 3, new int[] {0,2});
        
        return new Spectra(p_winSpecFile, PhysicsTools.UnitsPrefix.NANO.getMultiplier(), PhysicsTools.UnitsPrefix.UNITY.getMultiplier(), "csv", ",", 2, new int[] {0,1});
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
    
    public Spectra(HashMap p_function)
    {
        super(p_function);
    }
    
    private Spectra (File p_inputFile, BigDecimal p_abscissaUnitMultiplier, BigDecimal p_valuesUnitMultiplier, String p_expectedExtension, String p_separator, int p_ncolumn, int[] p_columnToExtract) throws FileNotFoundException, DataFormatException, ArrayIndexOutOfBoundsException, IOException
    {
        super(p_inputFile, p_abscissaUnitMultiplier, p_valuesUnitMultiplier, p_expectedExtension, p_separator, p_ncolumn, p_columnToExtract);
    }
    
    @Override
    public Spectra divide(BigDecimal p_dividend)
    {
        return new Spectra(super.divide(p_dividend));
    }
    
    @Override
    public Spectra avoidZeros()
    {
        return new Spectra(super.avoidZeros());
    }
    
    public Spectra substract(Spectra p_passedSpectra)
    {
        return new Spectra(super.substract(p_passedSpectra));
    }
    
    public Spectra multiply(Spectra p_passedSpectra)
    {
        return new Spectra(super.multiply(p_passedSpectra));
    }
    
    public Spectra divide(Spectra p_passedSpectra)
    {
        return new Spectra(super.divide(p_passedSpectra));
    }
    
    public void logToFile(File outputFile, BigDecimal valueMultiplier) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        
        writer.write("Wavelength (nm)\tAbsolute intensity (μW/nm)");
        for(BigDecimal currentAbscissa: m_abscissa)
        {
            BigDecimal convertedIntensity = m_values.get(currentAbscissa).multiply(valueMultiplier);
            
            writer.newLine();
            writer.write(currentAbscissa.divide(PhysicsTools.UnitsPrefix.NANO.getMultiplier())+"\t"+convertedIntensity);
        }
        writer.flush();
        writer.close();
    }
    
    public Spectra selectWindow(BigDecimal p_minAbscissa, BigDecimal p_maxAbscissa)
    {
        Map<BigDecimal, BigDecimal> resizedFunction = new HashMap(m_values);
        List<BigDecimal> abscissaList = new ArrayList(resizedFunction.keySet());
        int abscissaLastIndex = abscissaList.size() - 1;
        BigDecimal previous, next;
        
        if (abscissaList.get(1).compareTo(p_minAbscissa) < 0 || abscissaList.get(1).compareTo(p_maxAbscissa) > 0)
        {
            resizedFunction.remove(0);
        }
        for(int i = 1 ; i < abscissaLastIndex ; i += 1)
        {
            previous = abscissaList.get(i-1);
            next = abscissaList.get(i+1);
            
            if((previous.compareTo(p_minAbscissa) < 0 || previous.compareTo(p_maxAbscissa) > 0)     //previous not in range
                    && (next.compareTo(p_minAbscissa) < 0 || next.compareTo(p_maxAbscissa) > 0))    //next not in range
            {
                resizedFunction.remove(abscissaList.get(i));
            }
        }
        
        return new Spectra((HashMap) resizedFunction);
    }
}
