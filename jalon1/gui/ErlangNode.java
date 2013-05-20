package gui;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.ericsson.otp.erlang.*; 

public class ErlangNode extends OtpNode{
	
	OtpMbox mbox;
	static List<Noeuds> listNode=new ArrayList<Noeuds>();
	
	public ErlangNode(String nameNode) throws IOException {
		super(nameNode);
		// TODO Auto-generated constructor stub
	}

	public boolean ping(String serverName){
		if (this.ping(serverName,2000)) {
			System.out.println("remote is up");
			return true;
		}
		else {
			System.out.println("remote is not up");
			return false;
		} 
	}
	
	public OtpErlangPid receive(ErlangNode node,String boxname){
		//On crée la boîte mail de zou, boxname est la boîte mail du 
		//sera utiliser pour envoyer le message dans Erlang
		OtpErlangTuple msg;
		OtpErlangPid pidfrom;
		mbox=node.createMbox(boxname);
		OtpErlangObject objet;
		while(true){
			
			try{
				//attend l'envoie du message
				objet= mbox.receive();
				
				if(objet instanceof OtpErlangTuple){
					msg = (OtpErlangTuple)objet;
					pidfrom = (OtpErlangPid)(msg.elementAt(0));
					System.out.println("Receive something from {< "+pidfrom+" >, "+msg.elementAt(1)+" }");
					return pidfrom;
				}
			}
			
			catch(Exception e){
				System.out.println("Please check! Exception\n"+ e);
			}
		}
	}
	
	public OtpErlangPid receiveList(ErlangNode node,String boxname) throws OtpErlangExit, OtpErlangDecodeException{
		OtpNode myNode=null;
		OtpErlangList message;
		OtpErlangPid pidfrom;
		mbox=node.createMbox(boxname);
		OtpErlangObject objet;
		OtpErlangList list;
		while(true){
			objet= mbox.receive();
			
			//System.out.println(objet);
			message=(OtpErlangList) objet;
			//list=(OtpErlangList) message.elementAt(1); //recupère la liste
			for(OtpErlangObject elemList: message){
				OtpErlangTuple idHash_tupleName=(OtpErlangTuple) elemList;
				OtpErlangBinary code=(OtpErlangBinary) idHash_tupleName.elementAt(0);// code Hashé
				 
							OtpErlangTuple name_node=(OtpErlangTuple)idHash_tupleName.elementAt(1); // recupère l'élément associé au code hashé
							OtpErlangObject nom=(OtpErlangObject) name_node.elementAt(0); //recupère le nom du process
							
							System.out.println("code: "+code.hashCode()+" , "+nom.toString());
							listNode.add(new Noeuds(code.hashCode(),nom.toString()));
			}
			return null;
		}
		
	}
	
	static List<Noeuds> getList(){
		return listNode;
	}
	
	public void reply(OtpErlangPid pid){
		OtpErlangObject[] msgReply = new OtpErlangObject[2];
		msgReply[0] = mbox.self();
		msgReply[1] = new OtpErlangAtom("pong");
		System.out.println("My pid is < "+msgReply[0]+" >");
		System.out.println("Send message to < "+pid+" >");
		mbox.send(pid,msgReply[1]);
	}

	public static void main(String []args) throws Exception{
		
		final ErlangNode mynode=new ErlangNode("gui@localhost");
		mynode.ping("here@localhost"); 
		OtpErlangPid pid=mynode.receiveList(mynode, "mbox");
		//NodeView nodeView=new NodeView();
		
		EventQueue.invokeLater(new Runnable() {       	
        	
            @Override
            public void run() {
                try {
                	JFrame f = new JFrame();
                    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    f.add(new NodeView(mynode)); // n=5 affichage de n process
                    f.pack();
                    f.setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

	}
}
