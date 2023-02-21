package vehicleNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import DAGSrtucture.edgeClass;
import DAGSrtucture.theDAGClass;
import msgStruct.Tools;

public class test {
	
	public static void main(String args[]) throws IOException {
		theDAGClass dagClass=new theDAGClass(4, 0);
		dagClass.initDAG(4);
		
		dagClass.DAG[0][0][0].msgInfoString="000";
		dagClass.DAG[1][1][1].msgInfoString="111";
		dagClass.DAG[0][0][0].initFlag=true;
		Tools tools=new Tools();
		
		String jsonString=tools.threeDarray2Json(dagClass.DAG);
		Gson gson=new Gson();
		
		System.out.println(jsonString);
		edgeClass DAG2[][][]=new edgeClass[4][4][6];
		DAG2=tools.Json2DAG(jsonString);
		System.out.println(DAG2[0][0][0].msgInfoString);
		System.out.println(DAG2[1][1][1].msgInfoString);
		System.out.println(DAG2[0][0][0].initFlag);
		

		
	}
}
