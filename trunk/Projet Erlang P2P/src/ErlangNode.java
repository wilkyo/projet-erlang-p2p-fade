package graphics;
import java.io.IOException;

import com.ericsson.otp.erlang.*; 

public class ErlangNode extends OtpNode{
	
	OtpMbox mbox;
	
	
	public ErlangNode(String nameNode) throws IOException {
		super(nameNode);
		// TODO Auto-generated constructor stub
	}

	public boolean ping(){
		if (this.ping("sam@localhost",2000)) {
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
		OtpErlangObject o;
		while(true){
			
			try{
				//attend l'envoie du message
				o= mbox.receive();
				
				if(o instanceof OtpErlangTuple){
					msg = (OtpErlangTuple)o;
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
	

	
	public void reply(OtpErlangPid pid){
		OtpErlangObject[] msgReply = new OtpErlangObject[2];
		msgReply[0] = mbox.self();
		msgReply[1] = new OtpErlangAtom("pong");
		System.out.println("My pid is < "+msgReply[0]+" >");
		System.out.println("Send message to < "+pid+" >");
		mbox.send(pid,msgReply[1]);
	}

	public static void main(String []args) throws Exception{
		
		ErlangNode mynode=new ErlangNode("zou@localhost");
		mynode.ping();
		OtpErlangPid pid=mynode.receive(mynode, "mbox");			
		mynode.reply(pid);		
		
	}
}
