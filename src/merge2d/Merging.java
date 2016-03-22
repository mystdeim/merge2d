package merge2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.RecursiveTask;

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
	
	// linked list
	public void merge2(List<PointNode> list) {
		int size = list.size();
		long steps = 0;
		long ns = System.nanoTime();
		long ns_0 = 0;
		long r_ns = 0;
		long tmp_ns;
		boolean sorted = true;
		ListIterator<PointNode> iterator;
		while (sorted) {
			sorted = false;
			for (int i=0; i < list.size(); i++) {
				long tmp_ns_0 = System.nanoTime();
				PointNode parent = list.get(i);
				ns_0 += System.nanoTime() - tmp_ns_0;

				iterator = list.listIterator(i+1);
				while (iterator.hasNext()) {
					PointNode child = iterator.next();
					double range = getEffectedR(parent.getCount())  + getEffectedR(child.getCount());
					
					tmp_ns = System.nanoTime();
					if (parent.canMerge(child, range) ) {						
						parent.merge(child);
//						list.remove(child);
						iterator.remove();
						sorted = true;
					}
					r_ns += System.nanoTime() - tmp_ns;
					
					steps++;
				}
//				System.out.printf("Size: %d \n", list.size());
			}
		}
		System.out.printf("Merging 2: %d -> %d for %.2f (%.2f, %.2f) ms and %d steps \n", 
				size, list.size(), (System.nanoTime() - ns) / 1_000_000.0, 
				ns_0 / 1_000_000.0,
				r_ns / 1_000_000.0, steps );
		
	}
	
	// end to begin
	public void merge3(List<PointNode> list) {
		int size = list.size();
		long steps = 0;
		long ns = System.nanoTime();
		long r_ns = 0;
		long tmp_ns;
		boolean sorted = true;
		while (sorted) {
			sorted = false;
			for (int i=list.size()-1; i > 0; i--) {
				PointNode child = list.get(i);
				
				for (int j=i-1; j > -1; j--) {
					steps++;
					PointNode parent = list.get(j);
					double range = getEffectedR(parent.getCount())  + getEffectedR(child.getCount());
					
					if (parent.canMerge(child, range) ) {						
						parent.merge(child);
						tmp_ns = System.nanoTime();
						list.remove(i);
						r_ns += System.nanoTime() - tmp_ns;
						sorted = true;
						break;
					}
					
				}
			}
		}
		System.out.printf("Merging 3: %d -> %d for %.2f (%.2f) ms and %d steps \n", 
				size, list.size(), (System.nanoTime() - ns) / 1_000_000.0, r_ns / 1_000_000.0, steps );
		
	}
	
	public void sortByR(List<PointNode> list) {
		long ns = System.nanoTime();
		list.sort((PointNode a, PointNode b) -> {
			return Double.compare(a.getR(), b.getR());
		});
		System.out.printf("Sorting: %.2f \n", (System.nanoTime() - ns) / 1_000_000.0);
	}
	
	// merge after sort
	public void merge4(List<PointNode> list) {
		int size = list.size();
		long steps = 0;
		long ns = System.nanoTime();
		long r_ns = 0;
		long tmp_ns;
		boolean sorted = true;
		sortByR(list);
		while (sorted) {
			sorted = false;
			for (int i=0; i < list.size(); i++) {
				PointNode parent = list.get(i);

				ListIterator<PointNode> iterator = list.listIterator(i+1);
				while (iterator.hasNext()) {
					steps++;
					PointNode child = iterator.next();
					double range = getEffectedR(parent.getCount())  + getEffectedR(child.getCount());

					if (child.getR() - parent.getR() > range) {
						break;
					} else if (parent.canMerge(child, range) ) {						
//					if (parent.canMerge(child, range) ) {						
						parent.merge(child);
						tmp_ns = System.nanoTime();
						iterator.remove();
						r_ns += System.nanoTime() - tmp_ns;
						sorted = true;
					}
					
				}
//				System.out.printf("Size: %d \n", list.size());
			}
		}
		System.out.printf("Merging 4: %d -> %d for %.2f (%.2f) ms and %d steps \n", 
				size, list.size(), (System.nanoTime() - ns) / 1_000_000.0, 
				r_ns / 1_000_000.0, steps );
		
	}
	
	public void merge4p(List<PointNode> list) {
		int size = list.size();
		long steps = 0;
		long ns = System.nanoTime();
		long r_ns = 0;
		long tmp_ns;
		boolean sorted = true;
		sortByRParallel(list);
		while (sorted) {
			sorted = false;
			for (int i=0; i < list.size(); i++) {
				PointNode parent = list.get(i);
				
				ListIterator<PointNode> iterator = list.listIterator(i+1);
				while (iterator.hasNext()) {
					steps++;
					PointNode child = iterator.next();
					double range = getEffectedR(parent.getCount())  + getEffectedR(child.getCount());
					
					if (child.getR() - parent.getR() > range) {
						break;
					} else if (parent.canMerge(child, range) ) {						
						parent.merge(child);
						tmp_ns = System.nanoTime();
						iterator.remove();
						r_ns += System.nanoTime() - tmp_ns;
						sorted = true;
					}
					
				}
//				System.out.printf("Size: %d \n", list.size());
			}
		}
		System.out.printf("Merging 4: %d -> %d for %.2f (%.2f) ms and %d steps \n", 
				size, list.size(), (System.nanoTime() - ns) / 1_000_000.0, 
				r_ns / 1_000_000.0, steps );
		
	}
	
	
	// Parallel
	// -----------------------------------------------------------------------------------------------------------------
	
	public int steps;
	public static final int MIN_FOR_PARALLEL = 10_000;
	
	public void sortByRParallel(List<PointNode> list) {
		long ns = System.nanoTime();
		PointNode[] array = list.toArray(new PointNode[list.size()]);
		Arrays.parallelSort(array,(PointNode a, PointNode b) -> {
			return Double.compare(a.getR(), b.getR());
		});
		ListIterator<PointNode> iterator = list.listIterator();
        for (Object e : array) {
            iterator.next();
            iterator.set((PointNode) e);
        }
		System.out.printf("Parallel sorting: %.2f \n", (System.nanoTime() - ns) / 1_000_000.0);
	}
	
	public void mergeParallel(List<PointNode> list) {
		long ns = System.nanoTime();
		steps = 0;
		int size = list.size();
		boolean sorted = true;
		sortByRParallel(list);
		while (sorted) {
			sorted = false;
			if (list.size() < MIN_FOR_PARALLEL) {
				mergeSubList(list);
			} else {
				List<MergingTask> tasks = new ArrayList<>();
				int size_before = list.size();
				for (int i=0; i<list.size(); i+= MIN_FOR_PARALLEL) {
					MergingTask task = new MergingTask(list.subList(
							i, Math.min(i+MIN_FOR_PARALLEL, list.size()))
					);
					tasks.add(task);
				}
				list.clear();
				for (MergingTask task : tasks) task.fork();
//				list = new LinkedList<>();
				for (MergingTask task : tasks) list.addAll(task.join());
				if (size_before > list.size()) sorted = true;
			}
		}
		System.out.printf("Merging parallel: %d -> %d for %.2f ms \n", 
				size, list.size(), 	
				(System.nanoTime() - ns) / 1_000_000.0);
//		return list;
	}
	
	public void mergeSubList(List<PointNode> list) {
		boolean sorted = true;
		while (sorted) {
			sorted = false;
			for (int i=0; i < list.size(); i++) {
				PointNode parent = list.get(i);
				
				ListIterator<PointNode> iterator = list.listIterator(i+1);
				while (iterator.hasNext()) {
//					steps++;
					PointNode child = iterator.next();
					double range = getEffectedR(parent.getCount())  + getEffectedR(child.getCount());
					
					if (child.getR() - parent.getR() > range) {
						break;
					} else if (parent.canMerge(child, range) ) {						
						parent.merge(child);
						iterator.remove();
						sorted = true;
					}
					
				}
			}
		}
	}
	
	@SuppressWarnings("serial")
	private class MergingTask extends RecursiveTask<List<PointNode>> {

		public MergingTask(List<PointNode> list) {
			// TODO Если это не ставить, то будет многопоточная ошибка!
			this.list = new LinkedList<>(list);
//			this.list = list;
		}
		
		private List<PointNode> list;
		
		@Override
		protected List<PointNode> compute() {
			mergeSubList(list);
			return list;
		}
		
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	
	protected double getEffectedR(long count) {
		return PointNode.R_D + Helpers.rank(count) * PointNode.R_K;
	}

}
