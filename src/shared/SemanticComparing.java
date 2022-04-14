package shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import edu.umbc.web.Similarity;

public class SemanticComparing {
	public List<String> inList1 = new ArrayList<String>() ;
	public List<String> inList2 = new ArrayList<String>() ;
	public List<String> outList = new ArrayList<String>() ;
	private double d=0;
	private int difference =0;
	public final static double Threshold = 0.75;
	private Similarity sim;
	private boolean check= true;
	public SemanticComparing(List<String> l1, List<String> l2)
	{
		if (check){
		sim = new Similarity();
		check=false;
		}
		
		this.inList1 = l1;
		this.inList2 = l2;
	}
	
	public List<String> getOutList() throws IOException
	{
		removeValue();
		String check="";
		int l1 = inList1.size();
		int l2 = inList2.size();
		double a,max;
		int h=0;
	
		for(int i=0; i<l1; i++)
		{
			check="";
			a=0;max=0;
			for(int j=0; j<l2; j++)
			{
				if(i!=j+i)
				{
					a=SemSimilarity(inList1.get(i), inList2.get(j));
					if(a>max){ max=a; check= inList2.get(j); }
					System.out.println(inList1.get(i)+" =||= "+inList2.get(j)+" = "+ a);
				
				}

			}
			if(max<Threshold)
			{
				///
			}else{
				
				System.out.println(inList1.get(i)+"<=>"+check);
				h++;
			}
			
		}
		System.out.println("matched:::::"+h);
		 
		 return outList;
		
	}
	
	public double SemSimilarity(String a, String b) 
	{
	
		return sim.getSim(a, b);
	}
	
	public void removeValue()
	{
		for(int i=0; i<this.inList1.size(); i++)
		{
			if(this.inList1.get(i).toLowerCase().equals("global"))
			{
				this.inList1.remove(i);
			}
		}
		for(int i=0; i<this.inList2.size(); i++)
		{
			if(this.inList2.get(i).toLowerCase().equals("global"))
			{
				this.inList2.remove(i);
			}
		}
		
	}

}
