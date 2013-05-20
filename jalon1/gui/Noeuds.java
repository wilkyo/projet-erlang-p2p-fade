package gui;

public class Noeuds {
		String node_name;
		int IdHache;
		public String getNode_name() {
			return node_name;
		}
		public void setNode_name(String node_name) {
			this.node_name = node_name;
		}
		public int getIdHache() {
			return IdHache;
		}
		public void setIdHache(int idHache) {
			IdHache = idHache;
		}
		public Noeuds(int idHache,String node_name) {
			super();
			this.node_name = node_name;
			IdHache = idHache;
		}
		
		
		
}
