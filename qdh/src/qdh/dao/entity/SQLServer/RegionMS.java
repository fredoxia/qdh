package qdh.dao.entity.SQLServer;

import java.io.Serializable;
import java.util.Set;

public class RegionMS implements Serializable {
		private int regionID;
		private String name;
		private boolean deleted;

		public RegionMS(){
			regionID =0;
			name="";
			deleted = false;
		}
		public int getRegionID() {
			return regionID;
		}
		public void setRegionID(int regionID) {
			this.regionID = regionID;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean getDeleted() {
			return deleted;
		}
		public void setDeleted(boolean deleted) {
			this.deleted = deleted;
		}
		
}
