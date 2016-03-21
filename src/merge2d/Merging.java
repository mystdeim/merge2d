package merge2d;

import java.util.List;

public class Merging {
	
	public void stupid(List<PointNode> list) {
		long ns = System.nanoTime();
		int size = list.size();
		boolean sorted = true;
		while (sorted) {
			sorted = false;
			for (int i=0; i < list.size(); i++) {
				PointNode parent = list.get(i);
				
				for (int j=i+1; j < list.size(); j++) {
					PointNode child = list.get(j);
					double range = getEffectedR(parent.getCount())  + getEffectedR(child.getCount());
					
					if (parent.canMerge(child, range) ) {						
						parent.merge(child);
						list.remove(child);
						sorted = true;
					}
				}
			}	
		}
		System.out.printf("Stupid merging %d -> %d for %.2f ms \n", 
				size, list.size(), (System.nanoTime() - ns) / 1_000_000.0 );

	}
	
	protected double getEffectedR(long count) {
		return 5 + Helpers.rank(count) * 2;
	}

}
