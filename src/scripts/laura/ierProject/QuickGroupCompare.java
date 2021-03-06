package scripts.laura.ierProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

public class QuickGroupCompare
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = getGroupPValueMapForIER();
		addGroupPValueForSleeve(map);
		writeMap(map);
	}
	
	private static void writeMap( HashMap<String, Holder> map ) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getLauraDir() + File.separator + 
				"WhitneyComparison" + File.separator + "mergedPValues.txt")));
		
		writer.write("taxa\tierValGroupPValue\tierValTimePValue\tierTumorPValue\tierCoef\t" + 
		"sleeveValGroupPValue\tsleeveValTimePValue\tsleeveTumorPValue\tsleeveCoef\n");
		
		for( String s : map.keySet())
		{
			writer.write( s + "\t");
			Holder h = map.get(s);
			
			writer.write( writeORNA(h.ierVALGroup) + "\t" + writeORNA(h.ierValTime) + "\t" + 
								writeORNA(h.ierValTumor) + "\t" + writeORNA(h.ierCorrCoefficient) + "\t" + 
							writeORNA(h.sleeveValGroup) + "\t" + writeORNA(h.sleeveValTime) + "\t" + 
							writeORNA(h.sleeveValTumor) + "\t" +  writeORNA(h.sleeveCorrCoefficient) + 
							"\n");
		}
			
		writer.flush();  writer.close();
	}
	
	private static String writeORNA(Double val)
	{
		if( val == null)
			return "NA";
		
		return val.toString();
	}
	
	private static class Holder
	{
		Double ierVALGroup = null;
		Double ierValTime = null;
		Double ierValTumor = null;
		Double ierCorrCoefficient = null;
		Double sleeveValGroup =null;
		Double sleeveValTime = null;
		Double sleeveValTumor = null;
		Double sleeveCorrCoefficient = null;
	}
	
	private static HashMap<String, Holder> addGroupPValueForSleeve(HashMap<String, Holder> map) throws Exception
	{
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getLauraDir() + File.separator + 
				"SleeveGastroProject" + File.separator + "secondModelsBugsgenus.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s= s.replace("\"","");
			if( ! s.startsWith("MDS"))
			{
				String[] splits =s.split("\t");
				
				String key = splits[0];
				
				Holder h = map.get(key);
				
				if( h == null)
				{
					h = new Holder();
					map.put(key, h);
				}
				
				h.sleeveValTime = Double.parseDouble(splits[1]);
				h.sleeveValTumor  = Double.parseDouble(splits[6]);
				h.sleeveValGroup= Double.parseDouble(splits[3]);
				h.sleeveCorrCoefficient = Double.parseDouble(splits[7]);
			}
		}
		
		
		return map;
	}
	
	private static HashMap<String, Holder> getGroupPValueMapForIER() throws Exception
	{
		HashMap<String, Holder>  map = new HashMap<>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(ConfigReader.getLauraDir() + 
				File.separator + 
				"IER_Project" + File.separator +  "whitneyOut" + File.separator 
					+ "IER_genus_ModelComparisons_Vol2.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			s = s.replaceAll("\"", "");
			if( ! s.startsWith("MDS"))
			{
				String[] splits =s.split("\t");
				
				String key = splits[0];
				
				if( map.containsKey(key))
					throw new Exception("No");
				
				Holder h = new Holder();
				
				map.put(key, h);
				
				h.ierValTime = Double.parseDouble(splits[1]);
				h.ierValTumor = Double.parseDouble(splits[9]);
				h.ierVALGroup = Double.parseDouble(splits[3]);
				h.ierCorrCoefficient = Double.parseDouble(splits[11]);
				
			}
		}
		
		
		return map;
	}
}
