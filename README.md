# VANET-DAG-Sim
A simulator of the DAG-based data dissemination protocol used in VANET store-carry-forward

How to simulate locally:

1.Open project by Eclipse/IDEA  
2.Set totalVehicles, msgLostRate in main.java. Notice the maximum totalVehicles could be influenced by your computer performance. Too many totalVehicles could slow down your computer and the simulation.  
3.Run main.main

Example output:

Channel built. Waiting for connection...  
Listening thread started.  
Vehicle 0 running...  
Vehicle 1 running...  
Vehicle 2 running...  
Protocol start time: 1676999943408  
Vehicle 3 running...  
Received connection from: /127.0.0.1:7334  
Received connection from: /127.0.0.1:7335  
Received connection from: /127.0.0.1:7336  
Received connection from: /127.0.0.1:7337  
Vehicle0 go to round1  
Vehicle1 go to round1  
Vehicle2 go to round1  
Vehicle3 go to round1  
Vehicle0 go to round2  
Vehicle1 go to round2  
Vehicle2 go to round2  
Vehicle3 go to round2  
Vehicle0 go to round3  
Vehicle0 reaches last round, checking consistency  
Vehicle0 printing initial value. Protocol end time: 1676999943534  
["testvalue0","testvalue1","testvalue2","testvalue3"]  
Vehicle1 go to round3  
Vehicle1 reaches last round, checking consistency  
Vehicle1 printing initial value. Protocol end time: 1676999943538  
["testvalue0","testvalue1","testvalue2","testvalue3"]  
Vehicle2 go to round3  
Vehicle3 go to round3  
Vehicle2 reaches last round, checking consistency  
Vehicle2 printing initial value. Protocol end time: 1676999943541  
["testvalue0","testvalue1","testvalue2","testvalue3"]  
Vehicle3 reaches last round, checking consistency  
Vehicle3 printing initial value. Protocol end time: 1676999943542  
["testvalue0","testvalue1","testvalue2","testvalue3"]  
