package msgStruct;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import DAGSrtucture.edgeClass;

import javax.xml.crypto.Data;

public class Tools {
	
	public String threeDarray2Json(edgeClass DAG[][][]) {
		List <edgeClass> edgeList=new ArrayList<edgeClass>();

		String json=new Gson().toJson(DAG);
		return json;
	}
	public edgeClass[][][] Json2DAG(String json){
		Type type=new TypeToken<edgeClass[][][]>(){}.getType();
		edgeClass[][][] DAG=new Gson().fromJson(json, type);
		return DAG;
	}
	public edgeClass[][][] initialedDAG(int i, int j, int r){
		edgeClass[][][] DAG=new edgeClass[i][j][r];
		for (int k = 0; k < i; k++) {
			for (int k2 = 0; k2 < j; k2++) {
				for (int l = 0; l < r; l++) {
					edgeClass edge=new edgeClass();
						DAG[k][k2][l]=edge;
				}
			}
		}
		return DAG;
	}
	
	public void writeString(int length, String jString, DataOutputStream dOut) throws IOException {
		synchronized (jString){
			dOut.writeInt(length);
			dOut.write(jString.getBytes());
		}
		
		return;
	}
	public String serverReadfromStream(DataInputStream stream) throws IOException{
		int length=stream.readInt();
		byte[] data=new byte[length];
		stream.readFully(data);
		String rtnData=new String(data);
		return rtnData;
	}
}
