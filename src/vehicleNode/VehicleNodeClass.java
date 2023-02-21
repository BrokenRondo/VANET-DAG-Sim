package vehicleNode;
import DAGSrtucture.*;
import msgStruct.Tools;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

public class VehicleNodeClass extends Thread{
	private int serialNumber=0;
	private String initialValueString="";
	private static int startingPort=50000;
	private int vehicleNumber=4;//How many vehicles (nodes)
	private int byzantineMode=0;
	private static String serverHoString="127.0.0.1";
	private static int serverPort=49999;
	private static int maxRound=6;
	private theDAGClass DAGclass;
	public OutputStream outputStream;
	public DataOutputStream out;
	public InputStream inputStream;
	public DataInputStream in;
	private boolean processFlag=true;
	private boolean printFlag=false;

	public VehicleNodeClass(int serial,String init) {
		this.setSerialNumber(serial);
		this.setInitialValueString(init);		
	}
	public VehicleNodeClass(int serial,int Byz,int totalVehicleNumber) {
		this.setSerialNumber(serial);	
		this.byzantineMode=Byz;
		this.vehicleNumber=totalVehicleNumber;
		this.DAGclass=new theDAGClass(vehicleNumber,serialNumber);
		this.DAGclass.initDAG(vehicleNumber);


	}
	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getInitialValueString() {
		return initialValueString;
	}

	public void setInitialValueString(String initialValueString) {
		this.initialValueString = initialValueString;
	}
	

	public void braodCast(edgeClass[][][] DAG) {
		
	}
	@Override
	public void run() {
		System.out.println("Vehicle "+serialNumber+" running...\n");
		try {
			sleep(new Random().nextInt(5));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		initialValueString="testvalue"+serialNumber;
		theDAGClass DAG=new theDAGClass(vehicleNumber,serialNumber);
		try {
			Socket vehicleClient=new Socket(serverHoString, serverPort);//connect to server, which is to simulate the communication channel

			outputStream=vehicleClient.getOutputStream();
			out=new DataOutputStream(outputStream);
			inputStream=vehicleClient.getInputStream();
			in=new DataInputStream(inputStream);
			edgeClass DAGr1[][][]=new edgeClass[vehicleNumber][vehicleNumber][maxRound];
			DAGr1=new Tools().initialedDAG(vehicleNumber,vehicleNumber,maxRound);
			//start 1st round broadcast, physically send messages to server channel
			for (int i = 0; i < vehicleNumber; i++) {
				DAGr1[serialNumber][i][0].initFlag=true;
				DAGr1[serialNumber][i][0].msgInfoString=initialValueString;
				Tools tools=new Tools();
				String jsonString=tools.threeDarray2Json(DAGr1);
				int restlength=jsonString.length();//json string plus an int = 4 bytes, but minus 4 as rest length
				//String tstring=new Tools().PackString(restlength,jsonString);
				tools.writeString(restlength,jsonString,out);

			}
			while (processFlag) {
				//handle received messages
				int length=in.readInt();
				byte[] data=new byte[length];
				in.readFully(data);
				//每收到一条消息，就进行处理，查看是否能next round
				handleMsg(new String(data));
					
			}
			
		} catch (UnknownHostException e) {
			System.out.println("Host unknown");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void handleMsg(String jsonString) throws IOException {
		if (DAGclass.currentRound>=3) return;
		Tools tools=new Tools();
		edgeClass tDAG[][][]=new edgeClass[vehicleNumber][vehicleNumber][maxRound];
		tDAG=tools.initialedDAG(vehicleNumber, vehicleNumber, maxRound);
		tDAG=tools.Json2DAG(jsonString);
		for (int i = 0; i < vehicleNumber; i++) {
			for (int j = 0; j < vehicleNumber; j++) {
				for (int r = 0; r < maxRound; r++) {
					if (tDAG[i][j][r].initial()) {//if this edge exists
						this.DAGclass.addEdge(i, j, r, tDAG[i][j][r].msgInfoString);
					}
				}
			}
		}
		boolean advance=this.DAGclass.advance2NextRound();//try to go to next round after every received message
		if (advance) {
			System.out.println("Vehicle"+serialNumber+" go to round"+(this.DAGclass.currentRound));

			edgeClass[][][] curDAG=new Tools().initialedDAG(vehicleNumber, vehicleNumber, maxRound);
			for (int i = 0; i < vehicleNumber; i++) {
				for (int j = 0; j < vehicleNumber; j++) {
					for (int r = 0; r < DAGclass.currentRound; r++) {
						curDAG[i][j][r]=DAGclass.DAG[i][j][r];
					}
				}
			}


			for (int i = 0; i < vehicleNumber; i++) {

				edgeClass[][][] tDAG2=new Tools().initialedDAG(vehicleNumber,vehicleNumber,maxRound);
				tDAG2=curDAG;
				tDAG2[serialNumber][i][DAGclass.currentRound].initFlag=true;//
				String jsonString2=new Tools().threeDarray2Json(tDAG2);
				int restlength=jsonString2.length();//json string plus an int = 4 bytes, but minus 4 as rest length
				new Tools().writeString(restlength,jsonString2,out);
				//out.writeBytes(new Tools().PackString(restlength, jsonString2));
			}

		}
		if (DAGclass.currentRound==3) {
			if (DAGclass.currentRound==3)System.out.println("Vehicle"+serialNumber+" reaches last round, checking consistency");
			if(DAGclass.check1RoundAlltoAll()) {
				//System.out.println("Vehicle"+serialNumber+" printing DAG");
				String jsonDAG=new Tools().threeDarray2Json(DAGclass.DAG);
				//System.out.println(jsonDAG);

				if (!printFlag)
				{
					System.out.println("Vehicle"+serialNumber+" printing initial value. "+"Protocol end time: "+System.currentTimeMillis());
					DAGclass.printInitValue();
					printFlag=true;
				}
				//if(DAGclass.currentRound==3)this.processFlag=false;
			}else {
				System.out.println("Vechile"+serialNumber+" Consistency checking failed, round 1 does not reach all-to-all");
				this.processFlag=false;
			}
		}			
		
	}
	
	
}
