package DAGSrtucture;

import com.google.gson.Gson;

import msgStruct.Tools;

public class theDAGClass {
	private int verticesNumber=0;
	private static int roundNumber=6;//How many rounds in a DAG
	private int falseMax=1;//The maximum number of faulty nodes
	public edgeClass DAG[][][];
	public int currentRound=0;
	public int fatherVehicle=0;
	public theDAGClass(int vNumber,int vehicleSeries) {
		this.verticesNumber=vNumber;
		this.falseMax=verticesNumber/3;
		this.fatherVehicle=vehicleSeries;
		this.DAG=new edgeClass[vNumber][vNumber][roundNumber];
		initDAG(vNumber);
	}
	
	public void initDAG(int vehicleNumber)
	{
		for (int i = 0; i < vehicleNumber; i++) {
			for (int j = 0; j < vehicleNumber; j++) {
				for (int r = 0; r < roundNumber; r++) {
					this.DAG[i][j][r]=new edgeClass();
				}
			}
		}
	}
	public void addEdge(int sender, int receiver,int round, String msgInfo) {
		if (!DAG[sender][receiver][round].initial()) {
			DAG[sender][receiver][round]=new edgeClass(sender, receiver,round, msgInfo);
		}else {
			if (msgInfo.compareTo(DAG[sender][receiver][round].msgInfoString)!=0) {//if not equal
				System.out.println("Inconsistency detected: ("+sender+','+receiver+','+round+')');
				DAG[sender][receiver][round].msgInfoString="";
			}
		}
	}
	
	public boolean advance2NextRound() {
		int receivedMsg=0;
		for (int i = 0; i < verticesNumber; i++) {
			if (DAG[i][fatherVehicle][currentRound].initial()) {
				receivedMsg++;
			}
			if (receivedMsg>=(2*falseMax+1)) {
				currentRound++;
				return true;
			}
		}
		
		return false;		
	}
	
	public boolean check1RoundAlltoAll() {
		for (int i = 0; i < verticesNumber; i++) {
			for (int j = 0; j < verticesNumber; j++) {
				if (!DAG[i][j][0].initial()) {
					return false;
				}
			}
		}
		return true;
	}
	public void printInitValue() {
		String[] initValue=new String[verticesNumber];
		for (int i = 0; i < verticesNumber; i++) {
			initValue[i]=this.DAG[i][i][0].msgInfoString;
		}
		System.out.println(new Gson().toJson(initValue));
	}
	
	
}
