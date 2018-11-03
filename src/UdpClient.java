import java.io.*;
import java.net.*;

public class UdpClient extends Thread {

    public InetAddress ipAddress;
    public DatagramSocket socket;
    private Board board;
    public DatagramPacket packet1;
    public byte[] bytes;
    public int type;
    public  UdpClient(Board board,String ipAddress)
    {
        this.board=board;
        try {
            this.socket=new DatagramSocket();
            this.ipAddress=InetAddress.getByName((ipAddress));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
    public  void run()
    {
        while(true)
        {
            byte[] data=new  byte[1024];
            DatagramPacket packet = new DatagramPacket(data,data.length);
            DatagramPacket packet1 = new DatagramPacket(data,data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
              bytes=packet.getData();
             ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
             DataInputStream dataIn = new DataInputStream(byteIn);
            try {
                type = dataIn.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (type==1)
            {
                try {
                    board.NR=dataIn.readInt();
                    System.out.println(board.NR);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(type==2)
            {
                try {
                    board.inGame=dataIn.readBoolean();
                    if (board.inGame==false){
                        board.ready=false;}
                    System.out.println(board.inGame);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(type==3)
            {


                if(board.NR==1)
                {
                    try {
                        board.P2=dataIn.readInt();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        board.P1=dataIn.readInt();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(type==4)
            {
                try {
                    board.ball1.x=dataIn.readInt();
                    board.ball1.y=dataIn.readInt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (type==5)
            {
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                 DataOutputStream dataOut = new DataOutputStream(byteOut);
                try {
                    dataOut.writeInt(5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bytes=byteOut.toByteArray();
                sendData(bytes);
                System.out.println( "Time out ACK");
            }
            if (type==6)
            {
                board.inGame=false;
                board.ready=false;
                System.out.println(" Player time out" +
                        "");
            }

        }
    }
    public void sendData(byte[] data)
    {
        DatagramPacket packet= new DatagramPacket(data,data.length,ipAddress,1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}