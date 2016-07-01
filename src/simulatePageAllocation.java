import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/*
 * Program Name: simulatePageAllocation
 * Description: This program simulates PFF and VSWS algorithms
 * Developed By: Pallabi Chakraborty
 * References
 * 
 * Program Changes History
 * ----------------------------------------------------------------------------------------------------------------------------------------------
 * Date of Change                                                         Description of the Changes
 * ----------------------------------------------------------------------------------------------------------------------------------------------
 * 09-Dec-2015                                                            Initial Draft
 * ----------------------------------------------------------------------------------------------------------------------------------------------
 */


/************************************************************************************************************************************************
Observations from running both the Memory Allocation Algorithms
------------------------------------------------------------------
 * From the analysis of the runs, following are the observations
 * VSWS helps to keep the frames constant thus the maximum size of resident set stays less as compared to PFF
 * In case of PFF, as F increases, the memory usage increases as the resident set size can increase which is also the case with higher value of
 * M,L,Q in VSWS
 * 
 * Sample Output 
***********************************************************************************************************************************
Page Fault Frequency
***********************************************************************************************************************************
Simulation of PFF for F = 2 for process with 15261 pages:
Page Fault Count: 11282
Maximum size of resident set: 57
Number of times the resident set dipped lesser than 10: 815
---------------------------------------------------------------------------------------
Simulation of PFF for F = 50 for process with 15261 pages:
Page Fault Count: 255
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of PFF for F = 60 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of PFF for F = 100 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of PFF for F = 2500 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of PFF for F = 5000 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of PFF for F = 9999 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
***********************************************************************************************************************************
Variable Interval Sampled Working Set
***********************************************************************************************************************************
Simulation of VSWS for M = 4,L = 12, Q = 4 for process with 15261 pages:
Page Fault Count: 14407
Maximum size of resident set: 10
Number of times the resident set dipped lesser than 10: 15261
---------------------------------------------------------------------------------------
Simulation of VSWS for M = 20,L = 2000, Q = 1050 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of VSWS for M = 20,L = 2000, Q = 2 for process with 15261 pages:
Page Fault Count: 11203
Maximum size of resident set: 38
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of VSWS for M = 20,L = 900, Q = 100 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of VSWS for M = 50,L = 2000, Q = 1050 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of VSWS for M = 100,L = 2000, Q = 1050 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
Simulation of VSWS for M = 1000,L = 2000, Q = 1050 for process with 15261 pages:
Page Fault Count: 100
Maximum size of resident set: 100
Number of times the resident set dipped lesser than 10: 10
---------------------------------------------------------------------------------------
************************************************************************************************************************************************/
public class simulatePageAllocation {
	
	//Variable to store the charset to use for decoding
	static Charset charset = Charset.forName("ISO-8859-1");

	public static void main(String[] args) throws IOException {
		
		//Variable to store the path of the file containing the process file
		String processFile;
		//Flag to decide whether to generate processfile or simulatePageFrameAllocation
		boolean simulatePageFrameAllocation=true;
		
		//Change the flag simulatePageFrameAllocation to true to simulate PageFrame Allocation using VSWS and PFF
		//Change the flag to false to generate the Process text file
		if(simulatePageFrameAllocation==true)
		{
			 //Fetch the path of the Process File
			processFile=args[0];
			
			/************************************************************************************
			 * Approximation of F in PFF
			 * ---------------------------
			 *  For PFF instead of taking F as time, assuming F is the number of page fetches.
			 *  For around 1000 possible different values for the pages for a process containing
			 *  10000 pages a value of F=100 is a good approximation.
			 *  As the pages being accessed become more and more variant, then value of F may need to
			 *  be increased and vice versa
			 *  As the F increases the number of page faults goes on reducing, there are instances when
			 *  the number of page faults becomes constant and does not change with the increase in
			 *  F
			 *************************************************************************************/
			System.out.println("***********************************************************************************************************************************");
			System.out.println("Page Fault Frequency");
			System.out.println("***********************************************************************************************************************************");
			
			//Call PFF for F=30
			utilities.implementPFF(processFile, 2);
			
			//Call PFF for F=50
			utilities.implementPFF(processFile, 50);
			
			//Call PFF for F=60
			utilities.implementPFF(processFile, 60);
			
			//Call PFF for F=100
			utilities.implementPFF(processFile, 100);
			
			//Call PFF for F=5000
			utilities.implementPFF(processFile, 2500);
			
			//Call PFF for F=5000
			utilities.implementPFF(processFile, 5000);
			
			//Call PFF for F=9999
			utilities.implementPFF(processFile, 9999);
			
			/***********************************************************************************************************************************
			 * Approximation of M,L,Q for VSWS
			 * -----------------------------------
			 * For VSWS instead of taking M,L as time, assuming M,L as the number of page fetches.
			 * With M being too low, the number of page faults increases, as the values are flushed off more frequently
			 * The impact of Q depends on M, thus having a Q which is lesser than M is of no effect. After M has passed, having lesser value of Q 
			 * increases the number of page faults.
			 * L is of impact only when there are same pages being referred to again and again thus no/minimal page faults come up.
			 * For a process with around 100 pages and referenced around 10000 times, the values M = 20,L = 900, Q = 100 have been found to be fairly optimum
			 *************************************************************************************************************************************/
			System.out.println("***********************************************************************************************************************************");
			System.out.println("Variable Interval Sampled Working Set");
			System.out.println("***********************************************************************************************************************************");
			//Call with different values of M
			//Extremely less M and Q
			utilities.implementVSWS(processFile, 4, 12, 4);
			//Comparitively lesser value of M, moderate value of Q
			utilities.implementVSWS(processFile, 20, 2000, 1050);
			//Q value lesser than M
			utilities.implementVSWS(processFile, 20, 2000, 2);
			//Different combinations of M,L and Q
			utilities.implementVSWS(processFile, 20, 900, 100);
			utilities.implementVSWS(processFile, 50, 2000, 1050);
			utilities.implementVSWS(processFile, 100, 2000, 1050);
		    utilities.implementVSWS(processFile, 1000, 2000, 1050);
		}
		else
		{
			//Declare the path for the output file
			String outputTextFile=args[0];
			utilities.generateFile(outputTextFile);
		}
		
	}
	
	//Class containing the methods to be called in the main method
	public static class utilities{
		
		//Implement PFF using the Process text file
		public static void implementPFF(String processFile,int freqF) throws IOException
		{
			//Declare variable to count Page Faults
			int pageFaults=0;
			//Declare variable to keep track of the number of iterations after which Page Fault occurs
			int pageAccessCount=0;
			//Declare variable to store the maximum number of frames in the resident set
			int maxSize=0;
			//Declare variable to store the minimum value of resident set for which counts to be recorded
			int minSize=10;
			//Count of times when the residentSet went lesser than the minimum size
			int minSizeCount=0;
			// Count of Iteration
			//int iterationCount=0;


			//Read the process file
			/*Data generated in the process file of the form
			 * 16667
			 * 75
			 * 24
			 * 78
			 * 6
			 * 68
			 * ....
			 */
			/*The first integer is an indication to you how many pages the process occupies; 
			 * this will not change throughout the run of the program.  
			 * All subsequent integers are page references which the program makes during it execution.
			 */
			List<String> lines = Files.readAllLines(Paths.get(processFile), charset);
			Iterator<String> iter = lines.iterator();

			//Read the first value to know the number of pages available in the process
			String first = iter.next().toString();

			// Declare the array to map the frames in the resident set
			// First column in the row to contain the page number and the second column to contain use bit which is 0 or 1
			List<int[][]> residentSet=new ArrayList<int[][]>();

			/* 
			 * Page Fault Frequency
			 * 1) Requires a use bit to be associated with each page in memory, 
			 *    set to 1 when that page is accessed Threshold F defines acceptable time-between-faults
			 * 2) When a page fault occurs, the OS notes the virtual time since the last page fault for 
			 *    that process (requires a page-reference counter)
			 * 3) If < F add page to resident set (RS) Else shrink RS by removing any page with use bit = 0, 
			 *    reset all other use bits to 0
			 */
			//Paging used - Demand Paging, thus nothing available in the beginning of the process
			while(iter.hasNext())
			{

				int pageRequested=Integer.parseInt(iter.next());


				pageAccessCount++;
				
				//If the page being accessed not found in the resident set then raise a Page Fault
				if(!utilities.checkPgResidentSet(residentSet,pageRequested))
				{
					pageFaults++;
					//System.out.println("Page Access Count:"+String.valueOf(pageAccessCount-1)+" "+pageRequested);

					if(pageAccessCount>freqF)
					{
						//Remove the values with use bit=0 from the resident set
						int[][] set=new int[1][2];
						Iterator<int[][]> delIter=residentSet.iterator();
						while(delIter.hasNext())
						{
							set=delIter.next();
							//System.out.println(set[0][0]+" "+set[0][1]+" being checked for deletion "+pageRequested);
							//Checks the use bit if it is 0 or 1
							if(set[0][1]==0)
							{
								delIter.remove();
							}

						}

						delIter=null;


						//iterationCount++;

						for(int i=0;i<residentSet.size();i++)
						{
							set=residentSet.get(i);
							//Checks the use bit if it is 0 or 1
							if(set[0][1]==1)
							{
								int[][] insertdata=new int[1][2];
								insertdata[0][0]=set[0][0];
								insertdata[0][1]=0;
								residentSet.set(i, insertdata);
								//insertdata=residentSet.get(i);
								//System.out.println("Use Bit of "+insertdata[0][0]+" updated to "+insertdata[0][1]);
							}
						}
						
						/*
						System.out.println("--------------------------------------------------");
						System.out.println("Iteration Number: "+iterationCount);
						System.out.println("--------------------------------------------------");

						System.out.println("Resident Set Size: "+residentSet.size());
						System.out.println("Resident Set");
						for(int[][] selectdata:residentSet)
						{
							System.out.println(selectdata[0][0]+" "+selectdata[0][1]);
						}*/
					}
					
					
					
					//Add the data in the resident set
					int[][] data=new int[1][2];
					data[0][0]=pageRequested;
					data[0][1]=1;

					//Add the value into resident set
					residentSet.add(data);
					//System.out.println("Data "+pageRequested+" added to the resident set");

					if(maxSize<residentSet.size())
					{
						maxSize=residentSet.size();
					}

					pageAccessCount=0;

					

				}

				//Check if the resident set is lesser than minimum size as specified, then add counter
				if(residentSet.size()<=minSize)
				{
					minSizeCount++;
				}

			}

			
			/*iterationCount++;
			System.out.println("--------------------------------------------------");
			System.out.println("Iteration Number: "+iterationCount);
			System.out.println("--------------------------------------------------");

			System.out.println("Resident Set Size: "+residentSet.size());
			System.out.println("Resident Set");
			for(int[][] selectdata:residentSet)
			{
				System.out.println(selectdata[0][0]+" "+selectdata[0][1]);
			}*/
			 

			System.out.println("Simulation of PFF for F = "+freqF+" for process with "+first+" pages:");
			System.out.println("Page Fault Count: "+pageFaults);
			System.out.println("Maximum size of resident set: "+maxSize);
			System.out.println("Number of times the resident set dipped lesser than "+minSize+": "+minSizeCount);
			System.out.println("---------------------------------------------------------------------------------------");

			//Release resources
			iter=null;
			residentSet=null;

		}

		//Implement VSWS using the Process text file
		public static void implementVSWS(String processFile,int M, int L, int Q) throws IOException
		{
			//Declare variable to count Page Faults
			int pageFaults=0;
			//Declare variable to count interim Page Faults
			int interimPageFaults=0;
			//Declare variable to keep track of the number of iterations after which Page Fault occurs
			int pageAccessCount=0;
			//Declare variable to store the maximum number of frames in the resident set
			int maxSize=0;
			//Declare variable to store the minimum value of resident set for which counts to be recorded
			int minSize=10;
			//Count of times when the residentSet went lesser than the minimum size
			int minSizeCount=0;
			// Count of Iteration
			//int iterationCount=0;

			//Read the process file
			/*Data generated in the process file of the form
			 * 16667
			 * 75
			 * 24
			 * 78
			 * 6
			 * 68
			 * ....
			 */
			/*The first integer is an indication to you how many pages the process occupies; 
			 * this will not change throughout the run of the program.  
			 * All subsequent integers are page references which the program makes during it execution.
			 */
			List<String> lines = Files.readAllLines(Paths.get(processFile), charset);
			Iterator<String> iter = lines.iterator();

			//Read the first value to know the number of pages available in the process
			String first = iter.next().toString();

			// Declare the array to map the frames in the resident set
			// First column in the row to contain the page number and the second column to contain use bit which is 0 or 1
			List<int[][]> residentSet=new ArrayList<int[][]>();

			/*
			 * The VSWS policy is driven by three parameters:
			 * M : The minimum duration of the sampling interval
			 * L : The maximum duration of the sampling interval
			 * Q : The number of page faults that are allowed to occur between sampling instances
			 * 
			 * The VSWS policy is as follows:
			 * 1. If the virtual time since the last sampling instance reaches L , then suspend the
			 * process and scan the use bits.
			 * 2. If, prior to an elapsed virtual time of L , Q page faults occur,
			 *     a. If the virtual time since the last sampling instance is less than M , then wait
			 *        until the elapsed virtual time reaches M to suspend the process and scan the
			 *        use bits.
			 *     b. If the virtual time since the last sampling instance is greater than or equal to 
			 *     M , suspend the process and scan the use bits.
			 */
			//Paging used - Demand Paging, thus nothing available in the beginning of the process
			while(iter.hasNext())
			{

              /*
				if(pageAccessCount==0)
				{
					System.out.println("--------------------------------------------------");
					System.out.println("Iteration Number: "+iterationCount);
					System.out.println("--------------------------------------------------");

					System.out.println("Resident Set Size: "+residentSet.size()+",Page Faults:"+interimPageFaults);
					System.out.println("Resident Set");
					for(int[][] selectdata:residentSet)
					{
						System.out.println(selectdata[0][0]+" "+selectdata[0][1]);
					}
				}*/
                
				
				//If there are more than L page requests successfully carried out, then check the use bits and clear the resident set
				if(pageAccessCount>=L)
				{
					//Remove the values with use bit=0 from the resident set
					int[][] set=new int[1][2];
					Iterator<int[][]> delIter=residentSet.iterator();
					while(delIter.hasNext())
					{
						set=delIter.next();
						//System.out.println(set[0][0]+" "+set[0][1]+" being checked for deletion "+pageRequested);
						//Checks the use bit if it is 0 or 1
						if(set[0][1]==0)
						{
							delIter.remove();
						}

					}
					//Release resources
					delIter=null;

					for(int i=0;i<residentSet.size();i++)
					{
						set=residentSet.get(i);
						//Checks the use bit if it is 0 or 1
						if(set[0][1]==1)
						{
							int[][] insertdata=new int[1][2];
							insertdata[0][0]=set[0][0];
							insertdata[0][1]=0;
							residentSet.set(i, insertdata);
							//insertdata=residentSet.get(i);
							//System.out.println("Use Bit of "+insertdata[0][0]+" updated to "+insertdata[0][1]);
						}
					}

					pageAccessCount=0;
					interimPageFaults=0;
					//iterationCount++;

				}

				else
				{

					int pageRequested=Integer.parseInt(iter.next());

					pageAccessCount++;
					
					
					//If the page being accessed not found in the resident set then raise a Page Fault
					if(!utilities.checkPgResidentSet(residentSet,pageRequested))
					{
						
						pageFaults++;	
						interimPageFaults++;

						int[][] data=new int[1][2];
						data[0][0]=pageRequested;
						data[0][1]=1;

						//Add the value into resident set
						residentSet.add(data);
						//System.out.println("Data "+pageRequested+" added to the resident set");
						
						//System.out.println("pageAccessCount: "+pageAccessCount+", interimPageFaults:"+interimPageFaults+", pageRequested:"+pageRequested);


						if(pageAccessCount>=M && interimPageFaults>=Q)
						{
							//System.out.println("pageAccessCount: "+pageAccessCount+", interimPageFaults:"+interimPageFaults);
							//Remove the values with use bit=0 from the resident set
							int[][] set=new int[1][2];
							Iterator<int[][]> delIter=residentSet.iterator();
							while(delIter.hasNext())
							{
								set=delIter.next();
								//System.out.println(set[0][0]+" "+set[0][1]+" being checked for deletion "+pageRequested);
								//Checks the use bit if it is 0 or 1
								if(set[0][1]==0)
								{
									delIter.remove();
								}

							}
							//Release resources
							delIter=null;

							for(int i=0;i<residentSet.size();i++)
							{
								set=residentSet.get(i);
								//Checks the use bit if it is 0 or 1
								if(set[0][1]==1)
								{
									int[][] insertdata=new int[1][2];
									insertdata[0][0]=set[0][0];
									insertdata[0][1]=0;
									residentSet.set(i, insertdata);
									//insertdata=residentSet.get(i);
									//System.out.println("Use Bit of "+insertdata[0][0]+" updated to "+insertdata[0][1]);
								}
							}
							
							pageAccessCount=0;
							interimPageFaults=0;
							//iterationCount++;
						}
						
						
					}

					//Check if the resident set is lesser than minimum size as specified, then add counter
					if(residentSet.size()<=minSize)
					{
						minSizeCount++;
					}

					//Check for the maximum size of the resident set
					if(maxSize<residentSet.size())
					{
						maxSize=residentSet.size();
					}

				}
			}



			System.out.println("Simulation of VSWS for M = "+M+",L = "+L+", Q = "+Q+" for process with "+first+" pages:");
			System.out.println("Page Fault Count: "+pageFaults);
			System.out.println("Maximum size of resident set: "+maxSize);
			System.out.println("Number of times the resident set dipped lesser than "+minSize+": "+minSizeCount);
			System.out.println("---------------------------------------------------------------------------------------");

			//Release resources
			iter=null;
			residentSet=null;

		}
		
		
		
		//Method to check if a value exists in the resident set
		//If value is found then set Use bit to 1 for the value
		public static boolean checkPgResidentSet(List<int[][]> residentSet,int pageNumber)
		{
			boolean result=false;
			int i=0;
			
			//Check if the page being requested is available in the resident set
			for(int[][] set:residentSet)
			{
				if(set[0][0]==pageNumber)
				{
					result=true;
					break;
				}
				i++;
			}
			
			//if the matching page is found
			if(result==true)
			{
				int[][] data=new int[1][2];
				data[0][0]=pageNumber;
				data[0][1]=1;
				
				//If the page value matches then set the use bit to 1
				residentSet.set(i, data);
				//System.out.println("Use bit updated for "+pageNumber);
			}
			
			
			
			return result;
		}
		
		//Method to generate the process text file containing the pages which it accesses.
		public static void generateFile(String outputTextFile) throws IOException
		{
			{
				//Declaring limit for the number of page counts the process to access
				int startingLimitPageCount=10000;
				int endingLimitPageCount=11000;
				//Declaring limit for the page numbers the process to access
				int startingLimitPageAccess=0;
				int endingLimitPageAccess=100;
				
				//Generate an integer to indication to you how many pages the process occupies.
				Random rand = new Random();
				int  n = rand.nextInt(endingLimitPageCount) + startingLimitPageCount;

				int[] processArray=new int[n+1];

				//Put n in the Array as the first element
				processArray[0]=n;

				
				//Generate numbers
				for(int i=1;i<=n;i++)
				{
					processArray[i]=rand.nextInt(endingLimitPageAccess) + startingLimitPageAccess;
				}

				//Output Process Text File
				File outputFile=new File(outputTextFile);
				if (outputFile.exists())
				{
					outputFile.delete();
				}
				PrintWriter out = new PrintWriter(new FileWriter(outputTextFile, true), true);

				for(int i=0;i<n+1;i++)
				{
					out.write(String.valueOf(processArray[i])+"\n");
				}
				
				System.out.println("Text File Generated in the path "+outputTextFile);

				out.close();

			}
		}
		
	}

}
