package gui;

public class Noeuds {
		String node_name;
		long IdHache;
		public String getNode_name() {
			return node_name;
		}
		public void setNode_name(String node_name) {
			this.node_name = node_name;
		}
		public long getIdHache() {
			return IdHache;
		}
		public void setIdHache(long idHache) {
			IdHache = idHache;
		}
		public Noeuds(long idHache,String node_name) {
			super();
			this.node_name = node_name;
			IdHache = idHache;
		}
		
		
		
}
