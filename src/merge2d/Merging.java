package merge2d;

import java.util.List;
import java.util.ListIterator;

public class Merging {
	
	public void stupid(List<PointNode> list) {
		int size = list.size();
		long steps = 0;
		long ns = System.nanoTime();
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
					
					steps++;
				}
				System.out.printf("Size: %d \n", list.size());
			}	
		}
		System.out.printf("Stupid merging %d -> %d for %.2f ms and %d steps \n", 
				size, list.size(), (System.nanoTime() - ns) / 1_000_000.0, steps );

	}
	
	public void merge1(List<PointNode> list) {
		int size = list.size();
		long steps = 0;
		long ns = System.nanoTime();
		long r_ns = 0;
		long tmp_ns;
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
						tmp_ns = System.nanoTime();
						list.remove(child);
						r_ns += System.nanoTime() - tmp_ns;
						sorted = true;
					}
					
					steps++;
				}
			}
		}
		System.out.printf("Stupid merging %d -> %d for %.2f (%.2f) ms and %d steps \n", 
				size, list.size(), (System.nanoTime() - ns) / 1_000_000.0, r_ns / 1_000_000.0, steps );
		
	}
	
	public void merge2(List<PointNode> list) {
		int size = list.size();
		long steps = 0;
		long ns = System.nanoTime();
		long r_ns = 0;
		long tmp_ns;
		boolean sorted = true;
		while (sorted) {
			sorted = false;
			for (int i=0; i < list.size(); i++) {
				PointNode parent = list.get(i);
				
				ListIterator<PointNode> iterator = list.listIterator(i+1);
				while (iterator.hasNext()) {
					PointNode child = iterator.next();
					double range = getEffectedR(parent.getCount())  + getEffectedR(child.getCount());
					
					if (parent.canMerge(child, range) ) {						
						parent.merge(child);
						tmp_ns = System.nanoTime();
//						list.remove(child);
						iterator.remove();
						r_ns += System.nanoTime() - tmp_ns;
						sorted = true;
					}
					
					steps++;
				}
				System.out.printf("Size: %d \n", list.size());
			}
		}
		System.out.printf("Stupid merging %d -> %d for %.2f (%.2f) ms and %d steps \n", 
				size, list.size(), (System.nanoTime() - ns) / 1_000_000.0, r_ns / 1_000_000.0, steps );
		
	}
	
	public void merge3(List<PointNode> list) {
		int size = list.size();
		long steps = 0;
		long ns = System.nanoTime();
		long r_ns = 0;
		long tmp_ns;
		boolean sorted = true;
		while (sorted) {
			sorted = false;
			for (int i=0; i < list.size(); i++) {
				PointNode parent = list.get(i);
				
				ListIterator<PointNode> iterator = list.listIterator(i+1);
				while (iterator.hasNext()) {
					PointNode child = iterator.next();
					double range = getEffectedR(parent.getCount())  + getEffectedR(child.getCount());
					
					if (parent.canMerge(child, range) ) {						
						parent.merge(child);
						tmp_ns = System.nanoTime();
//						list.remove(child);
						iterator.remove();
						r_ns += System.nanoTime() - tmp_ns;
						sorted = true;
					}
					
					steps++;
				}
			}
		}
		System.out.printf("Stupid merging %d -> %d for %.2f (%.2f) ms and %d steps \n", 
				size, list.size(), (System.nanoTime() - ns) / 1_000_000.0, r_ns / 1_000_000.0, steps );
		
	}
	
	protected double getEffectedR(long count) {
		return PointNode.R_D + Helpers.rank(count) * PointNode.R_K;
	}

}
