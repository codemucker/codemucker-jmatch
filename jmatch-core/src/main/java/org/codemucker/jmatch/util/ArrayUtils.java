package org.codemucker.jmatch.util;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

	public static <T> List<T> toListOrNull(T[] arr){
		if(arr == null){
			return null;
		}
		List<T> list = new ArrayList<>(arr.length);
		for(int i = 0; i < arr.length;i++){
			list.add(arr[i]);
		}
		return list;
	}

}
