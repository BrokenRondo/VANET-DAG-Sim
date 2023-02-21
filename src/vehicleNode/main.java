package vehicleNode;
import DAGSrtucture.edgeClass;
import DAGSrtucture.theDAGClass;
import channelServer.channelServerClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static int totalVehicles=16;
    private static int serverPort=49999;
    private static double msgLostRate=0.74;

    public static void main(String args[]) throws IOException {
        channelServerClass channel=new channelServerClass(serverPort,msgLostRate);
        channel.start();
        //channel.startServer(serverPort);


        List<VehicleNodeClass> vehicleNodes=new ArrayList<VehicleNodeClass>();
        for (int i=0;i<totalVehicles;i++){
            VehicleNodeClass vehicle=new VehicleNodeClass(i,0,totalVehicles);
            vehicle.start();
            vehicleNodes.add(vehicle);
        }
        System.out.println("Protocol start time: "+System.currentTimeMillis());

    }
}
