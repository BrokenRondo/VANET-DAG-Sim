package channelServer;
import msgStruct.Tools;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class channelServerClass extends Thread{
    //private static double msgLostRate=1;
    private BlockingQueue<String> msgPool;
    public InputStream inputStream;
    public OutputStream outputStream;
    public DataInputStream dIn;
    public DataOutputStream dOut;
    public ServerSocket channelServer;
    public List<channelServerThread> threadPool;
    public static int serverPort=49999;
    public static int startingPort=50000;
    public double lostRate=0.5;
    public boolean processFlag=true;
    private int totalMsg=0;
    private int lostMsg=0;
    private static int channelCapacity=20000;
    public channelServerClass(int serverPort, double lostRate) throws IOException {
        msgPool=new ArrayBlockingQueue<String>(channelCapacity);
        threadPool=new ArrayList<channelServerThread>();
        channelServer=new ServerSocket(serverPort);
        this.lostRate=lostRate;

    }
    synchronized public void addMsgQueue (String jsonString) throws InterruptedException {
        msgPool.put(jsonString);
    }

    private class channelServerThread extends Thread{
        private Socket client;
        private String recvdJson;
        public InputStream inputStream;
        public DataInputStream dIn;
        public OutputStream outputStream;
        public DataOutputStream dOut;
        private int serialNumber;
        private boolean processFlag=true;//the flag to stop the thread
        public channelServerThread(Socket client) throws IOException {
            this.client=client;
            inputStream=client.getInputStream();
            dIn=new DataInputStream(inputStream);
            outputStream=client.getOutputStream();
            dOut=new DataOutputStream(outputStream);
            serialNumber=client.getLocalPort()-startingPort;

        }

        @Override
        public void run() {
            Tools tools=new Tools();
            while(processFlag){

                try {
                    String jsonFromVehicle=tools.serverReadfromStream(dIn);
                    addMsgQueue(jsonFromVehicle);
                    //System.out.println("add"+msgPool.size());
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        public void stopThread(){
            this.processFlag=false;
        }
    }

    private class channelListeningThread extends Thread{
        @Override
        public void run() {
            System.out.println("Listening thread started.");
            while (true) {
                try {
                    Socket client = channelServer.accept();
                    System.out.println("Received connection from: " + client.getRemoteSocketAddress());
                    channelServerThread channelServerThread = new channelServerThread(client);
                    threadPool.add(channelServerThread);
                    channelServerThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean msgLostOracle(double rate){//to control if the message is lost, true=lost
        double oracleDouble=new Random(System.currentTimeMillis()+new Random().nextInt()).nextDouble();
        //System.out.println(oracleDouble);
        if (oracleDouble<rate)
            return true;
        else return false;
    }

//    public void startServer(int port) throws IOException {
//
//
//
//
//        while(processFlag){
//            Socket client=channelServer.accept();
//            System.out.println("Received connection from: "+client.getRemoteSocketAddress());
//            channelServerThread channelServerThread=new channelServerThread(client);
//            threadPool.add(channelServerThread);
//            channelServerThread.start();
//
//        }
//    }

    @Override
    public void run() {
        System.out.println("Channel built. Waiting for connection...");
        new channelListeningThread().start();
        try {
            sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("Protocol start time: "+System.currentTimeMillis());
        while(true){
            if (!msgPool.isEmpty()){
                try {
                    String msg=msgPool.take();
                    //String msgTobeBroad=new Tools().PackString(msg.length(),msg);
//                    synchronized (msg) {
//                        channelBroadcastVehicles(msg);
//                    }
                    randomChannelBroadcastVehicles(msg);
                    //channelBroadcastVehicles(msg);
                    sleep(0);

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void channelBroadcastVehicles(String strBroadcast) throws IOException {
        for (channelServerThread i:threadPool){
            totalMsg++;
            if (!msgLostOracle(lostRate)) {
                i.dOut.writeInt(strBroadcast.length());
                i.dOut.write(strBroadcast.getBytes());
            }
            else{
                lostMsg++;
            }
            //System.out.println("Lost messages : total messages="+lostMsg+':'+totalMsg);
        }
    }
    public void randomChannelBroadcastVehicles(String strBroadcast) throws IOException, InterruptedException {

        List<Integer> random=new ArrayList<Integer>();
        for (int i=0;i<threadPool.size();i++){
            random.add(i);
        }


        //int count=threadPool.size();
        while(!random.isEmpty()){
            sleep(0);
            int randomThread=new Random().nextInt(random.size());
            //System.out.println("randomThread:"+randomThread);
            totalMsg++;
            if (!msgLostOracle(lostRate)) {
                synchronized (threadPool) {
                    threadPool.get(random.get(randomThread)).dOut.writeInt(strBroadcast.length());
                    threadPool.get(random.get(randomThread)).dOut.write(strBroadcast.getBytes());
                }

            }
            else{
                lostMsg++;
            }
            random.remove(randomThread);

        }

    }
    public void serverStop(){
        this.processFlag=false;
        for (channelServerThread i: threadPool){
            i.stopThread();
        }
    }
}
