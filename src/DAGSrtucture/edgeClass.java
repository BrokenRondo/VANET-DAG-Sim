package DAGSrtucture;

public class edgeClass {
	public String msgInfoString="";
	public int sender=0;
	public int receiver=0;
	public int round=0;
	public boolean initFlag=false;//To show if this edge is empty (received any message?)
	public edgeClass(int sender,int receiver,int round, String msgInfo) {
		this.sender=sender;
		this.receiver=receiver;
		this.round=round;
		this.msgInfoString=msgInfo;
		this.initFlag=true;
	}
	public edgeClass() {
		
	}
	
	public boolean initial() {
		return this.initFlag;
	}
}
