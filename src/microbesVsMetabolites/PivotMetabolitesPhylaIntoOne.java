package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import microbesVsMetabolites.WriteTrialsForSVMLight.MetaboliteClass;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class PivotMetabolitesPhylaIntoOne
{
	private static int TRAIL_NUMBER = 0;
	
	private static String alphaNumericOnly(String s)
	{
		s =s.replaceAll("\"", "");
		StringBuffer buff =new StringBuffer();
		
		for(int x=0; x < s.length(); x++)
		{
			char c= s.charAt(x);
			if(  Character.isAlphabetic(c) || Character.isDigit(c))
			{
				buff.append("" + c);
			}
			else
			{
				buff.append("_");
			}
		}
		System.out.println(s + " " + buff.toString());
		
		if (buff.toString().equals("X"))
		{
			TRAIL_NUMBER++;
			return "X" + TRAIL_NUMBER;
		}
		
		return buff.toString();
	}
	
	public static void main(String[] args) throws Exception
	{
		for(String s : WritaAsPhyla.LEVELS)
			pivotALevel(s);
	}
	
	public static void pivotALevel(String level) throws Exception
	{
		System.out.println(level);
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator 
					+ level +  "AsColumns.txt");
		MetaboliteClass mc = MetaboliteClass.URINE;
		
		List<String> metaboliteNames = getNames(mc);
		
		HashMap<Integer, List<Double>> metMap = WriteTrialsForSVMLight.getMetabolites(mc, false);
		
		if( wrapper.getSampleNames().size() != metMap.size())
			throw new Exception("No " + wrapper.getSampleNames().size() + " " + metMap.size());
		
		for(Integer i : metMap.keySet())
			if( metMap.get(i).size() != metaboliteNames.size())
				throw new Exception("No");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"merged" + level +  "_" + mc + "_AsColumnsLogNorm.txt")));
		
		writer.write("sample");
		
		for(String s : wrapper.getOtuNames())
			writer.write("\t" + s);
		
		for(String s : metaboliteNames)
			writer.write("\t" + alphaNumericOnly(s));
		
		writer.write("\n");
		
		for(String sample : wrapper.getSampleNames())
		{
			writer.write(sample);
			
			int sampleID = wrapper.getIndexForSampleName(sample);
			
			for( int x=0; x < wrapper.getOtuNames().size(); x++)
				writer.write("\t" + wrapper.getDataPointsNormalizedThenLogged().get(sampleID).get(x));
			
			List<Double> list = metMap.get(Integer.parseInt(sample.replace("sample", "")));
			
			if( list == null || list.size() != metaboliteNames.size())
				throw new Exception("No");
			
			for( int x=0; x < list.size(); x++)
				writer.write("\t" + list.get(x));
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	
	}
	
	static List<String> getNames(MetaboliteClass metaboliteClass) throws Exception
	{
		List<String> list = new ArrayList<String>();
		BufferedReader reader = null;
		
		if( metaboliteClass.equals(MetaboliteClass.URINE))
		{
			reader = 
					new BufferedReader(new FileReader(new File( 
							ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
							"urine_metabolites_data.txt")));
					
		}
		else if ( metaboliteClass.equals(MetaboliteClass.PLASMA))
		{
			reader = 
					new BufferedReader(new FileReader(new File( 
							ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
							"plasmaMetabolites.txt")));
		}
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			list.add(sToken.nextToken());
		}
		
		return list;
	}
}
