package io.github.jonathanxd.wreport.utils.list;

public class MaxLinkedListSizeExceeded extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3960654859472796030L;

	public MaxLinkedListSizeExceeded(String message, int size, MaxLinkedList<?> maxLinkedList) {
		super(String.format("%s. Size: %d. ListMaxSize: %d. Oversized: %d. Required Size: %d",
				message,
				size,
				maxLinkedList.getMaxListSize(), 
				getOverSize(size, maxLinkedList.getMaxListSize()),
				getRequiredSize(size, maxLinkedList.getMaxListSize())
				));
	}

	private static int getOverSize(int size, int maxListSize) {
		return (size - maxListSize);
	}

	private static int getRequiredSize(int size, int maxListSize) {
		return getOverSize(size, maxListSize) + maxListSize;
	}
}
